/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('dynamic.com.calm.cms.ui.DataTableWindow.${id}', {
    extend: 'Ext.Window',
	//引用需要的类
    requires: [
        'Ext.tree.Panel',
        'Ext.form.field.Text',
        'com.calm.cms.module.TableDefined',
        'com.calm.cms.module.TableColumn',
        'com.calm.platform.Utils'
    ],
	//基本属性
    title: '${title}',
    width:${width},
    height:480,
	
    animCollapse:false,
    constrainHeader:true,
    layout: {
        type: 'fit',
        padding: 5
    },
    initComponent:function(){
    	var me=this;
    	var store=Ext.create('Ext.data.Store', {
    		id:'data-table-${id}-grid-store',
            pageSize: 15,
            autoLoad:true,
            fields:${fields},
            proxy: {
                type: 'ajax',
                url : 'cms/table/data/list/${id}',
                reader: {
                    type: 'json',
                    root: 'list',
					idProperty:'proxyId',
                    totalProperty: 'total'
                }
            }
        });
    	var grid=Ext.create('Ext.grid.Panel', {
    		id:'data-table-${id}-grid',
			columns:[<#list columns as c>{"text":"${c.text}","width":${c.width},"dataIndex":"${c.dataIndex}"},
				</#list>{
    				 xtype:'actioncolumn',
    				 width:50,
    				 items: [{
						iconCls: 'edit',
						tooltip: '修改',
						handler: function(grid, rowIndex, colIndex) {
							//获得对话框
							var editorWindow=me.createEditorWindow();
							//获得表单在的panel
							var detailPanel = editorWindow.getComponent('cms-table-${id}-data-form-panel');
							//获得选中的数据
							var rec = grid.getStore().getAt(rowIndex);
							//获得表单，并把数据加载到表单内
							var form=detailPanel.getForm();
							form.url='cms/table/data/update/${id}/'+rec.get('proxyId')+"/";
							form.loadRecord(rec);

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
    	                			com.calm.platform.Utils.requestAjax('cms/table/data/delete/${id}/'+rec.get('proxyId'),{},function(){
    	                				store.reload();
										//grid.getView().refresh();
    	                			});
    	                		}
    	                	});
    	                }
					}]
			   }],
			store:store,
			dockedItems: [{
    	        xtype: 'pagingtoolbar',
    	        store:store,
    	        dock: 'bottom',
    	        displayInfo: true
    	    }]
		});
    	me.items=[grid];
		me.tools=[{
			id:'plus',
			handler:function(e, target, panel){
				var win=me.createEditorWindow();
				win.show();
			}
        }];
		me.fieldStores={
			<#list stores as c><#if c_index gt 0>,</#if>
			store_${c}:Ext.create('Ext.data.Store', {
				fields : ['displayValue', 'displayName'],
				autoLoad:true,
				proxy: {
					type: 'ajax',
					url : 'cms/table/data/many2one/${id}/${c}',
					reader: {
						type: 'json',
						idProperty:'displayValue',
					}
				}	
			})
			</#list>
		};
    	me.callParent();
    },
	createEditorWindow:function(){
    	var me=this;
		var form =Ext.create('Ext.form.Panel', {
			url: 'cms/table/data/add/${id}/',
			id:'cms-table-${id}-data-form-panel',
			layout:{
				type:'form',
				padding: 5
			},
			// The fields
			items:[<#list editorColumns as c><#if c_index gt 0>,</#if>
				{
					'layout':'${c.layout}',
					'items':[<#list c.items as s1c><#if s1c_index gt 0>,</#if>{
						"layout": "form",
						"columnWidth": 0.5,
						"items": [<#list s1c.items as s2c><#if s2c_index gt 0>,</#if>{
							"name": "${s2c.name}",
							"xtype": "${s2c.xtype}",
							"fieldLabel": "${s2c.fieldLabel}",
							<#if s2c.store??>store:${s2c.store},</#if>
							<#if s2c.displayField??>displayField:"${s2c.displayField}",</#if>
							<#if s2c.valueField??>valueField:"${s2c.valueField}",</#if>
							<#if s2c.editable??>editable:${s2c.editable},</#if>
							<#if s2c.inputValue??>inputValue:"${s2c.inputValue}",</#if>
							<#if s2c.vtype??>vtype:"${s2c.vtype}",</#if>
						}</#list>]
					}</#list>]
				}
			</#list>],
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
								var grid=Ext.getCmp('data-table-${id}-grid');
								grid.getStore().load();
								//me.grid.getView().refresh();
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
		win = Ext.create("Ext.Window",{
			id: 'cms-table-${id}-data-editor-win',
			title: '${title}详细信息',
			width:840,
			height:500,
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
		return win;
	}

});
