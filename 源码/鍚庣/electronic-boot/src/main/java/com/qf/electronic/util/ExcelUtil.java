package com.qf.electronic.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.analysis.ExcelReadExecutor;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.qf.electronic.excel.ExcelProcessor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static final int MAX_COUNT_PER_SHEET =  3000;

    private static final int MAX_BATCH_PROCESS_COUNT = 2000;//最大的批处理条数

    public static <T> boolean importExcel(InputStream is, Class<T> clazz, ExcelProcessor<T> processor){
        List<T> dataList = new ArrayList<>();
        Counter counter = new Counter();
        //每读取一行都会被监听
        ReadListener<T> listener = new ReadListener<T>() {

            @Override
            public void invoke(T data, AnalysisContext context) {//这个方法是读取每一行数据触发
                dataList.add(data);
                counter.totalData++; //记录总条数
                if(dataList.size() == MAX_BATCH_PROCESS_COUNT){//读取的数量达到写入数据库的批处理条数上限
                    counter.saveCount += processor.saveData(dataList); //记录保存成功的条数
                    dataList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {//这个方法是解析完每一个sheet都会触发
            }
        };
        ExcelReaderBuilder builder = EasyExcel.read(is, clazz, listener);
        ExcelReader reader = builder.build();
        //读取Excel的执行器
        ExcelReadExecutor excelReadExecutor = reader.excelExecutor();
        //通过执行器读取到Excel的所有sheet列表
        List<ReadSheet> readSheets = excelReadExecutor.sheetList();
        //遍历Sheet列表，挨个读取
        for(ReadSheet readSheet: readSheets){
            reader.read(readSheet);//读取sheet
        }
        //在所有sheet读取完成之后，如果集合中还存在数据，那么这些需要保存在数据库中
        if(dataList.size() > 0){
            counter.saveCount += processor.saveData(dataList);
        }
        return counter.saveCount == counter.totalData;
    }


    public static <T> void exportExcel(String sheetName, OutputStream os, Class<T> clazz, List<T> dataList){
        ExcelWriterBuilder writerBuilder = EasyExcel.write(os, clazz);
        ExcelWriter writer = writerBuilder.build();
        int sheetCount = dataList.size() / MAX_COUNT_PER_SHEET;
        if(dataList.size() % MAX_COUNT_PER_SHEET > 0){
            sheetCount += 1;
        }
        for(int i=0; i<sheetCount; i++){
            WriteSheet sheet = new WriteSheet();
            sheet.setSheetNo(i);
            sheet.setSheetName(sheetName + (i+1));
            //需要计算当前sheet存储的数据
            int start = i * MAX_COUNT_PER_SHEET;
            int end = (i+1) * MAX_COUNT_PER_SHEET;
            if(end > dataList.size()){
                end = dataList.size();
            }
            writer.write(dataList.subList(start, end), sheet);
        }
        //写入完成，关闭相关的流
        writer.finish();
    }

    static class Counter {
        long saveCount;
        long totalData;
    }
}
