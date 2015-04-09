package com.calm.cms.api.processor;

import java.util.List;

import com.calm.cms.api.entity.TableColumn;

public interface ListableFieldProcessor<T> extends FieldProcessor{
	List<T> getList(Integer id, TableColumn tableColumn);
}
