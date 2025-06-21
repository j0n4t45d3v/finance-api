package br.com.managementfinanceapi.adapter.out.report.xlsx.style;

import org.apache.poi.xssf.usermodel.XSSFColor;

public enum Color {
  HEADER("#2F5597"), // Azul escuro
  POSITIVO("#228B22"), // Verde escuro
  NEGATIVO("#C00000"), // Vermelho escuro
  TOTAL("#FFD966"), // Dourado claro
  ZEBRA("#EFEFEF"), // Cinza claro
  WHITE("#FFFFFF"); // branco

  private final XSSFColor value;
  Color(String hex) {
      hex = hex.replace("#", "");
      this.value = new XSSFColor(new byte[] {
          (byte) Integer.parseInt(hex.substring(0, 2), 16),
          (byte) Integer.parseInt(hex.substring(2, 4), 16),
          (byte) Integer.parseInt(hex.substring(4, 6), 16)
      });
    }

  public XSSFColor getValue() {
    return this.value;
  }
}