package com.example.demo5.utils;

import com.example.demo5.models.ModuleSolar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface CodeGenerator {

    void generateCode(ModuleSolar moduleSolar) throws IOException, WriterException;
}
