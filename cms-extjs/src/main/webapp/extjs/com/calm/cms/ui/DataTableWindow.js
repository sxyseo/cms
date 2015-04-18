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
    fields:null,
    height:480,
    columns:null,
    animCollapse:false,
    constrainHeader:true,
    layout: {
        type: 'fit',
        padding: 5
    },
    initComponent:function(){
    	var me=this;
    	var store=Ext.create('Ext.data.Store', {
    		id:'data-table-grid-store',
            pageSize: 15,
            autoLoad:true,
            fields:me.fields,
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
    	var grid=Ext.create('Ext.grid.Panel', {
    		id:'data-table-grid',
			columns:me.columns,
			store:store,
			dockedItems: [{
    	        xtype: 'pagingtoolbar',
    	        store:store,
    	        dock: 'bottom',
    	        displayInfo: true
    	    }]
		});
    	me.items=[grid];
    	me.callParent();
    },
//    show:function(){
//    	var me=this;
//    	com.calm.platform.Utils.requestAjax("cms/tableDefined/load",{id:me.tableId},function(data){
//    		var entity=data.entity;
//    		me.setTitle(entity.name);
//    		var columns=entity.columns;
//    		var items=[],fields=[];
//    		for(var i=0;i<columns.length;i++){
//    			var col=columns[i];
//    			var item=me.column2Items(col);
//    			if(item){
//    				items.push(item);
//    				if(col.relation=='MANY2ONE'){
//    					fields.push(columns[i].id.id+'_ID');
//        			}else{
//        				fields.push(columns[i].id.id);
//        			}
//    			}
//    		}
//    		var DATA_MODEL="Data"+me.tableId;
//    		var dataModel = Ext.ModelManager.getModel(DATA_MODEL);
//    		if(dataModel){
//    			dataModel=Ext.define(DATA_MODEL, {
//        		    extend: 'Ext.data.Model',
//        		    fields: fields
//        		});
//    		}
//    		
//    		me.setWidth(fields.length*200+11);
//    		
//    		var grid=Ext.getCmp("data-table-grid");
//    		var store=grid.store;
//    		store.model=dataModel;
//    		store.getProxy().url='cms/tableData/list?tableId='+me.tableId ,
//    		store.load();
//    		grid.reconfigure(store, items);
//    	});
//    	me.callParent();
//    },
//    column2Items:function(c){
//    	if(c.relation=='ONE2MANY'){
//    		return null;
//    	}
//    	if(c.relation=='MANY2ONE'){
//    		return {
//                text: c.name,
//                dataIndex: c.id.id+"_ID",
//                width:200
//           }
//    	}
//    	if(c.processor.type='table'){
//    		return {
//                text: c.name,
//                dataIndex: c.id.id,
//                width:200
//           }
//    	}else{
//    		return {
//                text: c.name,
//                dataIndex: c.id.id,
//                width:200
//           }
//    	}
//    }
});
