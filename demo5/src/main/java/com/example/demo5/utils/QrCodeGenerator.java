package com.example.demo5.utils;
import com.example.demo5.models.ModuleSolar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.var;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QrCodeGenerator implements CodeGenerator{
    @Override
    public void generateCode(ModuleSolar moduleSolar) throws IOException, WriterException {

        ObjectMapper objectMapper = new ObjectMapper();
        String moduleJson = objectMapper.writeValueAsString(moduleSolar);

        String qrCodePath = "D:\\solar-module\\QRCode\\";
        String qrCodeName = moduleSolar.getNommodule()+moduleSolar.getId()+"-QRCODE.png";
        var qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(moduleJson, BarcodeFormat.QR_CODE, 400, 400);
        Path path = FileSystems.getDefault().getPath(qrCodeName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) throws IOException, WriterException {
        QrCodeGenerator qrCodeGenerator = new QrCodeGenerator();
        ModuleSolar moduleSolar = new ModuleSolar();
        moduleSolar.setId(1L);
        moduleSolar.setNommodule("module1");

        qrCodeGenerator.generateCode(moduleSolar);
    }
}
