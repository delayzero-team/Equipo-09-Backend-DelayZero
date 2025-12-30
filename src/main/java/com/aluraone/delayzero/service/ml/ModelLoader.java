package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ModelLoader {

    public OrtSession loadModel(){
        try {
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            OrtSession.SessionOptions opt = new OrtSession.SessionOptions();

            try(InputStream stream = getClass().getResourceAsStream("resources/mlresource/model.onnx")){
                if(stream != null){
                    byte[] model = stream.readAllBytes();
                    return env.createSession(model, opt);
                }
            }
        } catch(IOException | OrtException ignored){ return null; }
        return null;
    }

}
