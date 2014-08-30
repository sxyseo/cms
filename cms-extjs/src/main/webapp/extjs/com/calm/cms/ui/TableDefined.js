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
    				 width:50,
    				 items: [{
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
                        	Ext.Msg.confirm("系统信息","你确定要删除?",function(buttonId,text,opt){
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
    }
});
