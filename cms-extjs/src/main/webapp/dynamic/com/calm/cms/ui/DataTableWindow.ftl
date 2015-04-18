/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('dynamic.com.calm.cms.ui.DataTableWindow-${id}', {
    extend: 'Ext.Window',

    requires: [
        'Ext.tree.Panel',
        'Ext.form.field.Text',
        'com.calm.cms.module.TableDefined',
        'com.calm.cms.module.TableColumn',
        'com.calm.platform.Utils'
    ],
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
    		id:'data-table-grid-store',
            pageSize: 15,
            autoLoad:true,
            fields:${fields},
            proxy: {
                type: 'ajax',
                url : 'cms/tableData/list?tableId=${id}',
                reader: {
                    type: 'json',
                    root: 'list',
                    totalProperty: 'total'
                }
            }
        });
    	var grid=Ext.create('Ext.grid.Panel', {
    		id:'data-table-grid',
			columns:${columns},
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
    }
});
