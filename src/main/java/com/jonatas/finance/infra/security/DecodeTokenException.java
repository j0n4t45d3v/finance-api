package com.jonatas.finance.infra.security;

public class DecodeTokenException extends IllegalStateException {

  public DecodeTokenException(String s) {
    super(s);
  }

  public static void throwTokenExpiredException() {
    throw new TokenExpiredException();
  }

  public static void throwInvalidTypeException() {
    throw new InvalidTypeException();
  }

  public static class TokenExpiredException extends DecodeTokenException {

    public TokenExpiredException() {
      super("Token is expired");
    }
  }

  public static class InvalidTypeException extends DecodeTokenException {

    public InvalidTypeException() {
      super("Invalid token type");
    }
  }
}
