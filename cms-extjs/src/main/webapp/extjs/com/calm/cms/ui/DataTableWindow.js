/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('com.calm.cms.ui.DataTableWindow', {
    extend: 'Ext.Window',

    requires: [
        'Ext.tree.Panel',
        'Ext.form.field.Text',
        'com.calm.cms.module.TableDefined',
        'com.calm.cms.module.TableColumn',
        'com.calm.platform.Utils'
    ],
    title: '数据管理',
    width:840,
    tableId:null,
    height:480,
    initComponent:function(){
    	var me=this;
    	var grid=Ext.create('Ext.grid.Panel', {
    		id:'data-table-grid',
			columns:[],
			items:[]
		});
    	me.items=[grid];
    	me.callParent();
    },
    show:function(){
    	var me=this;
    	com.calm.platform.Utils.requestAjax("cms/tableDefined/load",{id:me.tableId},function(data){
    		var entity=data.entity;
    		me.setTitle(entity.name);
    		var columns=entity.columns;
    		var items=[],fields=[];
    		for(var i=0;i<columns.length;i++){
    			var item=me.column2Items(columns[i]);
    			if(item){
    				items.push(item);
    				fields.push(columns[i].id.id);
    			}
    		}
    		me.setWidth(fields.length*200+11);
    		var store=Ext.create('Ext.data.Store', {
    			fields : fields,
                autoLoad: true,
                pageSize: 15,
                proxy: {
                    type: 'ajax',
                    url : 'cms/tableData/list?tableId='+me.tableId ,
                    reader: {
                        type: 'json',
                        root: 'list',
                        totalProperty: 'total'
                    }
                }
            });
    		Ext.getCmp("data-table-grid").reconfigure(store, items);
    	});
    	me.callParent();
    },
    column2Items:function(c){
    	if(c.relation=='ONE2MANY'){
    		return null;
    	}
    	if(c.processor.type='table'){
    		return {
                text: c.name,
                dataIndex: c.id.id,
                width:200
           }
    	}else{
    		return {
                text: c.name,
                dataIndex: c.id.id,
                width:200
           }
    	}
    }
});
