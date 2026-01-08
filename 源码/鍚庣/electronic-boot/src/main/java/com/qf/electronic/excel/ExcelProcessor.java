package com.qf.electronic.excel;

import java.util.List;

public interface ExcelProcessor<T> {

    int saveData(List<T> dataList);
}
