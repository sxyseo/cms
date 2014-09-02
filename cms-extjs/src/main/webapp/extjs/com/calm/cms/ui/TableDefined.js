/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('com.calm.cms.ui.TableDefined', {
    extend: 'com.calm.module.Module',

    requires: [
        'Ext.tree.Panel',
        'Ext.form.field.Text',
        'com.calm.cms.module.TableDefined',
        'com.calm.cms.module.TableColumn',
        'com.calm.platform.Utils'
    ],

    id: 'cms-table-defined',
    
    init : function(){
        this.launcher = {
            text: '模型管理',
            iconCls:'cms-table-defined-icon'
        };
    },
    grid:null,
    createGrid : function(){
    	var me=this;
//    	if(!me.grid){
        	var userStore=Ext.create('Ext.data.Store', {
                model: 'com.calm.cms.module.TableDefined',
                autoLoad: true,
                pageSize: 15,
                proxy: {
                    type: 'ajax',
                    url : 'cms/tableDefined/list',
                    reader: {
                        type: 'json',
                        root: 'list',
                        totalProperty: 'total'
                    }
                }
            });
        	me.grid =Ext.create('Ext.grid.Panel', {
        	    store:userStore,
        	    columns: [{
		                    text: '模型名称',
		                    dataIndex: 'name',
		                    width:200
	    	           }, {
	    	        	   text: '描述',
	    	               dataIndex: 'description',
	    	               //动态列宽
	    	               flex:1
	    	          }, {
    				 xtype:'actioncolumn',
    				 width:60,
    				 items: [{
    					iconCls: 'cms-table-defined-action-table-column',
     	                tooltip: '编辑列',
     	                handler: function(grid, rowIndex, colIndex) {
//     	                	//获得对话框
     	                	var editorWindow=me.createTableColumnWindow();
     	                	
     	                	//获得选中的数据
     	                    var rec = grid.getStore().getAt(rowIndex);
     	                   editorWindow.title='['+rec.get('name')+']模型项目';
     	                    var tableColumnGrid=Ext.getCmp('cms-table-defined-table-column-grid');
     	                    var store=tableColumnGrid.getStore();
     	                    editorWindow.tableId=rec.get('id');
     	                    store.load({params:{tableId:rec.get('id')}});

     	                    editorWindow.show();
     	                }
     	            },{
   					iconCls: 'edit',
 	                tooltip: '修改',
 	                handler: function(grid, rowIndex, colIndex) {
 	                	//获得对话框
 	                	var editorWindow=me.createEditorWindow();
                     	//获得表单在的panel
 	                	var detailPanel = editorWindow.getComponent('cms-table-defined-add-panel');
 	                	//获得选中的数据
 	                    var rec = grid.getStore().getAt(rowIndex);
 	                    //获得表单，并把数据加载到表单内
 	                    var form=detailPanel.getForm();
 	                    form.url='cms/tableDefined/update';
 	                    form.loadRecord(rec);
 	                    var field=form.findField("id");
 	                    field.readOnly = true;

 	                    editorWindow.show();
 	                	}
     	            },{
    	            	iconCls: 'remove',
    	                tooltip: '删除',
    	                isDisabled :function(grid, rowIndex, colIndex,item ,record ){
    	                	return record.data.systemDefined;
    	                },
    	                handler: function(grid, rowIndex, colIndex) {
    	                	Ext.Msg.confirm("系统信息","你确定要删除?",function(buttonId,text,opt){
    	                		if(buttonId=='yes'){
    	                			var rec = grid.getStore().getAt(rowIndex);
    	                			com.calm.platform.Utils.requestAjax('cms/tableDefined/delete',{id: rec.get('id')},function(){
    	                				userStore.reload();
    	                				grid.getView().refresh();
    	                			});
    	                		}
    	                	});
    	                }
    	            }]
    		}],
        	    dockedItems: [{
                    xtype: 'toolbar',
                    items: [{
                        iconCls: 'add',
                        text: '添加',
                        scope: this,
                        handler: function(){
                        	var editorWindow=this.createEditorWindow();
                        	var detailPanel = editorWindow.getComponent('cms-table-defined-add-panel');
                        	var form=detailPanel.getForm();
                        	form.reset();
                        	form.url='cms/tableDefined/add';
                        	editorWindow.show();
                        }
                    }, {
                    	iconCls: 'remove',
                        text: '删除',
                        disabled: true,
                        itemId:'cms-table-defined-btn-delete',
                        scope: this,
                        handler:function(){
                        	Ext.Msg.confirm("系统信息","你确定要删除?",function(buttonId){
    	                		if(buttonId=='yes'){
    	                			var view = me.grid.getView();
    	                			var selection = view.getSelectionModel().getSelection()[0];
    	                            if (selection) {
    	                            	com.calm.platform.Utils.requestAjax('cms/tableDefined/delete',{id: selection.raw.id},function(){
    	                            		userStore.reload();
    	                            		view.refresh();
        	                			});
    	                            }
    	                		}
    	                	});
                        }
                    }]
                },{
        	        xtype: 'pagingtoolbar',
        	        store: userStore,   // same store GridPanel is using
        	        dock: 'bottom',
        	        displayInfo: true
        	    }],
        	    listeners: {
        	    	//选中状态变化事件
                    'selectionchange': function(view, records) {
                    	if(records.length){
                    		var v=records[0];
                    		if(v.data.systemDefined){
                    			me.grid.down('#cms-table-defined-btn-delete').setDisabled(true);
                    			return;
                    		}
                    	}
                    	me.grid.down('#cms-table-defined-btn-delete').setDisabled(!records.length);
                    },
                    //行双击事件
                    itemdblclick:function(dataview, record, item, index, e){
                    	//获得对话框
                    	var editorWindow=me.createEditorWindow();
                    	//获得表单在的panel
                    	var detailPanel = editorWindow.getComponent('cms-table-defined-add-panel');
                    	var docked=detailPanel.getDockedItems()[0];
                    	docked.hide();
                    	//获得选中的数据
                        var rec = dataview.getStore().getAt(index);
                        //获得表单
                        var form =detailPanel.getForm();
                        //并把数据加载到表单内
                        form.loadRecord(rec);
                        //设置表单各项制度
                        form.getFields().each(function(field) {  
                            //设置只读  
                            field.setReadOnly(true);    
                        });
                        editorWindow.show();
                    }
                }
        	});
//    	}
    	return this.grid;
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('cms-table-defined-win');
        if (!win) {
            win = desktop.createWindow({
                id: 'cms-table-defined-win',
                title: '模型管理',
                width:840,
                height:480,
                iconCls: 'cms-table-defined-icon',
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                items: [ this.createGrid() ]
            });
        }

        return win;
    },
    createEditorWindow:function(){
    	var me=this;
    	var desktop = me.app.getDesktop();
        var win = desktop.getWindow('cms-table-defined-editor-win');
        if (!win) {
        	 var form =Ext.create('Ext.form.Panel', {
                 url: 'cms/tableDefined/add',
                 id:'cms-table-defined-add-panel',
                 layout: 'anchor',
                 defaults: {
                     anchor: '100%'
                 },
                 bodyPadding: 15,
                 // The fields
                 defaultType: 'textfield',
        		 items:[{
						name: 'id',
						xtype:'hiddenfield',
						fieldLabel:'编号'
					},{
						name: 'name',
					    fieldLabel:'模型名称',
					    allowBlank:false,
					    width:60
					},{
					   fieldLabel: '描述',
					   xtype : 'textareafield',
					   name: 'description'
					}],
					buttons: [{
	                    text: '取消',
	                    handler: function() {
	                        this.up('form').getForm().reset();
	                        this.up('window').close( );
	                    }
	                }, {
	                    text: '保存',
	                    handler: function() {
	                    	var form = this.up('form').getForm();
                    	 if (form.isValid()) {
                             form.submit({
                            	 waitMsg: '正在保存...',
                                 success: function(form, action) {
                                	 Ext.Msg.alert('系统信息', action.result.message);
                                	 me.grid.getStore().load();
                                	 me.grid.getView().refresh();
                                	 win.close();
                                 },
                                 failure: function(form, action) {
                                      Ext.Msg.alert('保存失败', action.result.message);
                                 }
                             });
                         }
                    }
                }]
        	 });
            win = desktop.createWindow({
                id: 'cms-table-defined-editor-win',
                title: '详细信息',
                width:440,
                height:300,
                modal:true,
                iconCls: 'cms-table-defined-icon',
                animCollapse:false,
                constrainHeader:true,
                layout: {
                    type: 'fit',
                    padding: 5
                },
                items: [form]
            });
        }
        return win;
    },
    createTableColumnWindow:function(){
		var me=this;
		var desktop = me.app.getDesktop();
		var win = desktop.getWindow('cms-table-defined-table-column-win');
		if (!win) {
			var grid=Ext.create('Ext.grid.Panel', {
				id:'cms-table-defined-table-column-grid',
				store:Ext.create('Ext.data.Store', {
					model: 'com.calm.cms.module.TableColumn',
					proxy: {
						type: 'ajax',
						url : 'cms/tableDefined/listAllTableColumn',
						reader: {
							type: 'json',
							root: 'list'
						}
					}
				}),
				columns:[{
					text: '列名',
					dataIndex: 'columnName',
					width:200
				},{
					text: '名称',
					dataIndex: 'name',
					width:200
				},{
					text: '关系',
					dataIndex: 'relationName',
					width:100
				},{
					text: '类型',
					dataIndex: 'processorName',
					width:100
				},{
					text: '默认值',
					dataIndex: 'defaultValue',
					width:100,
					renderer: function(value,metaData ,record ){
						var relation=record.get('relation');
						var processorType=record.get('processorType');
						if(processorType=='TABLE'){
							return "";
						}
						if(relation=='ONE2ONE'){
							return value;
						}
				        return '';
				    }
				},{
					text: '必要',
					dataIndex: 'required',
					width:50
				},{
					xtype:'actioncolumn',
					width:60,
					items: [{
						iconCls: 'edit',
						tooltip: '修改',
						handler: function(grid, rowIndex, colIndex) {
							//获得对话框
    	                	var editorWindow=me.createTableColumnEditorWindow();
    	                	
                        	//获得表单在的panel
    	                	var detailPanel = editorWindow.getComponent('cms-table-defined-table-column-editor-panel');
    	                	//获得选中的数据
    	                    var rec = grid.getStore().getAt(rowIndex);
    	                    //获得表单，并把数据加载到表单内
    	                    var form=detailPanel.getForm();
    	                    form.url='cms/tableColumn/update';
    	                    form.loadRecord(rec);
    	                    var field=form.findField("columnName");
    	                    field.readOnly = true;
    	                    form.findField('processorId').value=rec.get('processorId');
    	                    
    	                    editorWindow.show();
    	                    
						}
					},{
						iconCls: 'remove',
						tooltip: '删除',
						handler: function(grid, rowIndex, colIndex) {
							Ext.Msg.confirm("系统信息","你确定要删除?",function(buttonId,text,opt){
    	                		if(buttonId=='yes'){
    	                			var rec = grid.getStore().getAt(rowIndex);
    	                			com.calm.platform.Utils.requestAjax('cms/tableColumn/delete',{tableId: rec.get('tableId'),id: rec.get('columnName')},function(){
    	                				var tableColWin=Ext.getCmp('cms-table-defined-table-column-win');
    	                				grid.getStore().load({params:{tableId:tableColWin.tableId}});
//    	                				grid.getView().refresh();
    	                			});
    	                		}
    	                	});
						}
					}]
				}],
				dockedItems: [{
					xtype: 'toolbar',
					items: [{
						iconCls: 'add',
						text: '添加',
						scope: this,
						handler: function(){
							var win=me.createTableColumnEditorWindow();
							var tableColWin=Ext.getCmp('cms-table-defined-table-column-win');
							var formPanel=Ext.getCmp('cms-table-defined-table-column-editor-panel');
							var form =formPanel.getForm();
							form.findField('tableId').setValue(tableColWin.tableId);
							form.findField('relation').setValue('ONE2ONE');
							win.show();
						}
					}]
				}]
			});
			win = desktop.createWindow({
				id: 'cms-table-defined-table-column-win',
				title: '模型项目',
				width:830,
				height:500,
				modal:true,
				tableId:null,
				iconCls: 'cms-table-defined-icon',
				animCollapse:false,
				constrainHeader:true,
				layout: {
					type: 'fit',
					padding: 5
				},
				items: [grid]
			});
		}
        return win;
    },
    createTableColumnEditorWindow:function(){
    	var me=this;
    	var desktop = me.app.getDesktop();
        var win = desktop.getWindow('cms-table-defined-table-column-editor-win');
        if (!win) {
        	var form =Ext.create('Ext.form.Panel', {
        		url: 'cms/tableColumn/add',
                id:'cms-table-defined-table-column-editor-panel',
                layout: 'anchor',
                defaults: {
                    anchor: '100%'
                },
                bodyPadding: 15,
                // The fields
                defaultType: 'textfield',
        		items:[{
					name: 'tableId',
					xtype:'hiddenfield',
					fieldLabel:'模型ID'
        		},{
					name: 'columnName',
				    fieldLabel:'列名',
				    allowBlank:false,
				    regex : /^(([a-z]|[A-Z]|_)([a-z]|[A-Z]|_|[0-9])*)$/,
				    regexText : '列名只能以a-z或A-Z或_开头切只能包含字母数字和下划线(_)!'
				},{
					name: 'name',
					allowBlank:false,
				    fieldLabel:'名称'
				},{
					xtype:'fieldcontainer',
					layout: 'hbox',
					fieldLabel:'关系',
					combineErrors: true,
					items:[{
						name: 'relation',
					    xtype:'combobox',
					    flex: 1,
					    forceSelection: true, 
					    queryMode: 'local',
					    displayField: 'name',
					    valueField: 'id',
					    typeAhead:true,
					    editable: false,
					    store:Ext.create('Ext.data.Store', {
					    	fields : ['id', 'name'],
					    	data : [{"id" : "ONE2ONE", "name" : "一对一"},{"id" : "ONE2MANY", "name" : "一对多"},{"id" : "MANY2MANY", "name" : "多对多"}]
					    }),
					    listeners:{
					    	change:function(combo,  newValue, oldValue, eOpts){
					    		var editorWindow=Ext.getCmp('cms-table-defined-table-column-editor-win');
					    		var detailPanel = editorWindow.getComponent('cms-table-defined-table-column-editor-panel');
					    		var form=detailPanel.getForm();
					    		var field=form.findField('processorId');
					    		var store=field.getStore();
					    		if('ONE2ONE' == newValue){
					    			store.load();
					    		}else if('ONE2MANY' == newValue){
					    			store.load({params:{type:'TABLE'}});
					    		}else if('MANY2MANY' == newValue){
					    			store.load({params:{type:'TABLE'}});
					    		}
					    	}
					    }
					},{
						name: 'processorId',
						xtype:'combobox',
						margins: '0 0 0 6',
						flex: 1,
						allowBlank:false,
						forceSelection: true, 
					    queryMode: 'local',
					    displayField: 'name',
					    valueField: 'id',
					    typeAhead:true,
					    store:Ext.create('Ext.data.Store', {
					    	fields:['id', 'name','type','tableDefinedId'],
					    	model: 'com.calm.cms.module.FieldType',
					    	proxy: {
			                    type: 'ajax',
			                    url : 'cms/fieldType/listWithFilterType',
			                    reader: {
			                        type: 'json',
			                        root: 'list'
			                    }
			                },
			                listeners:{
			                	load:function( store, records, successful, eOpts ){
			                		if(records && records.length){
			                			var editorWindow=Ext.getCmp('cms-table-defined-table-column-editor-win');
							    		var detailPanel = editorWindow.getComponent('cms-table-defined-table-column-editor-panel');
							    		var form=detailPanel.getForm();
							    		var field=form.findField('processorId');
							    		if(field.getValue()){
							    			var store=field.getStore();
							    			var rec=store.getById(field.getValue());
							    			if(!rec){
							    				var f=store.first();
							    				if(f){
							    					field.setValue(f.get('id'));
							    				}
							    			}
							    		}else{
							    			field.setValue(records[0].get('id'));
							    		}
							    		
			                		}
			                	}
			                }
					    }),
					    listeners:{
					    	change:function(combo,  newValue, oldValue, eOpts){
					    		var data=combo.getStore().getById(newValue);
					    		if(data){
					    			var editorWindow=Ext.getCmp('cms-table-defined-table-column-editor-win');
						    		var detailPanel = editorWindow.getComponent('cms-table-defined-table-column-editor-panel');
						    		var form=detailPanel.getForm();
						    		var field=form.findField('defaultValue');
						    		var relation=form.findField('relation');
						    		var relationColumn=form.findField('relationColumn');
						    		if(relation.getValue()=='ONE2ONE'){
						    			if(data.data.type=='SIMPLE'){
							    			field.show();
							    		}else{
							    			field.hide();
							    		}
						    			relationColumn.hide();
						    			relationColumn.allowBlank=true;
						    		} else if(relation.getValue()=='ONE2MANY'){
						    			field.hide();
						    			relationColumn.show();
						    			relationColumn.allowBlank=false;
						    			if(combo.valueModels.length){
						    				relationColumn.getStore().load({params:{tableId:combo.valueModels[0].get('tableDefinedId')}});
						    			}
						    			
						    		} else if(relation.getValue()=='MANY2MANY'){
						    			field.hide();
						    			relationColumn.hide();
						    			relationColumn.allowBlank=true;
						    		}
						    		
					    		}
					    	},
					    	select:function(combo, value, option){
					    	}
					    }
					}]
				},{
					name: 'relationColumn',
					xtype:'combobox',
				    fieldLabel:'关联列',
				    allowBlank:false,
					forceSelection: true, 
				    queryMode: 'local',
				    displayField: 'name',
				    valueField: 'columnName',
				    typeAhead:true,
				    store:Ext.create('Ext.data.Store', {
				    	fields:['columnName', 'name'],
				    	model: 'com.calm.cms.module.TableColumn',
				    	proxy: {
		                    type: 'ajax',
		                    url : 'cms/tableDefined/listAllTableColumn',
		                    reader: {
		                        type: 'json',
		                        root: 'list'
		                    }
		                },
		                listeners:{
		                	load:function( store, records, successful, eOpts ){
		                		if(records && records.length){
		                			var editorWindow=Ext.getCmp('cms-table-defined-table-column-editor-win');
						    		var detailPanel = editorWindow.getComponent('cms-table-defined-table-column-editor-panel');
						    		var form=detailPanel.getForm();
						    		var field=form.findField('relationColumn');
						    		if(!field.getValue()){
						    			field.setValue(records[0].get('columnName'));
						    		}
		                		}
		                	}
		                }
				    })
				},{
					name: 'defaultValue',
				    fieldLabel:'默认值'
				},{
					name: 'required',
				    fieldLabel:'必须填写',
				    xtype:'checkbox'
				}],
				buttons: [{
					text: '取消',
					handler: function() {
						this.up('form').getForm().reset();
						this.up('window').close( );
					}
				}, {
					text: '保存',
					handler: function() {
						var form = this.up('form').getForm();
						if (form.isValid()) {
							form.submit({
								waitMsg: '正在保存...',
								success: function(form, action) {
									Ext.Msg.alert('系统信息', action.result.message);
									var tableColWin=Ext.getCmp('cms-table-defined-table-column-win');
									var tableColGrid=Ext.getCmp('cms-table-defined-table-column-grid');
//									
									tableColGrid.getStore().load({params:{tableId:tableColWin.tableId}});
									tableColGrid.getView().refresh();
									win.close();
								},
								failure: function(form, action) {
									Ext.Msg.alert('保存失败', action.result.message);
								}
							});
						}
					}
				}]
        	});
            win = desktop.createWindow({
                id: 'cms-table-defined-table-column-editor-win',
                title: '模型项目详细信息',
                width:440,
                height:300,
                modal:true,
                iconCls: 'cms-table-defined-icon',
                animCollapse:false,
                constrainHeader:true,
                layout: {
                    type: 'fit',
                    padding: 5
                },
                items: [form]
            });
        }
        return win;
    },
    loadTableColumns:function(fieldTypeId){
    }
});
