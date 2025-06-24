package br.com.managementfinanceapi.adapter.out.report.xlsx.page;

import org.apache.poi.ss.usermodel.Workbook;

public interface XlsxPage<T> {

  void createPage(Workbook workbook, T pageStruct);

}