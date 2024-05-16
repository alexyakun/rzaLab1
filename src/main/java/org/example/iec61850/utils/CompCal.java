package org.example.iec61850.utils;

import org.example.iec61850.datatypes.common.Vector;
import java.util.List;

public class CompCal {
    public static Vector mult(Vector a, Vector b){
        double realA = a.getMag().getF().getValue()*Math.cos(a.getAng().getF().getValue());
        double realB = b.getMag().getF().getValue()*Math.cos(b.getAng().getF().getValue());
        double imA = a.getMag().getF().getValue()*Math.sin(a.getAng().getF().getValue());
        double imB = b.getMag().getF().getValue()*Math.sin(b.getAng().getF().getValue());
        double realRes = realA*realB - imA*imB;
        double imageRes = realA+imB + realB*imA;
        Vector result = new Vector();
        result.getMag().getF().setValue(Math.sqrt(realRes*realRes + imageRes*imageRes));
        if(realRes > 0){
            result.getAng().getF().setValue(Math.atan(imageRes / realRes));
        } else if(realRes < 0 && imageRes >= 0){
            result.getAng().getF().setValue(Math.PI + Math.atan(imageRes / realRes));
        } else if(realRes < 0 && imageRes < 0){
            result.getAng().getF().setValue(-Math.PI + Math.atan(imageRes / realRes));
        } else if(realRes == 0 && imageRes > 0){
            result.getAng().getF().setValue(Math.PI/2);
        }else if(realRes == 0 && imageRes < 0){
            result.getAng().getF().setValue(-Math.PI/2);
        }
        return result;
    }
    public static Vector sum(Vector a, Vector b){
        double realA = a.getMag().getF().getValue()*Math.cos(a.getAng().getF().getValue());
        double realB = b.getMag().getF().getValue()*Math.cos(b.getAng().getF().getValue());
        double imA = a.getMag().getF().getValue()*Math.sin(a.getAng().getF().getValue());
        double imB = b.getMag().getF().getValue()*Math.sin(b.getAng().getF().getValue());
        double realRes = realA + realB;
        double imageRes = imA + imB;
        Vector result = new Vector();
        result.getMag().getF().setValue(Math.sqrt(realRes*realRes + imageRes*imageRes));
        if(realRes > 0){
            result.getAng().getF().setValue(Math.atan(imageRes / realRes));
        } else if(realRes < 0 && imageRes >= 0){
            result.getAng().getF().setValue(Math.PI + Math.atan(imageRes / realRes));
        } else if(realRes < 0 && imageRes < 0){
            result.getAng().getF().setValue(-Math.PI + Math.atan(imageRes / realRes));
        } else if(realRes == 0 && imageRes > 0){
            result.getAng().getF().setValue(Math.PI/2);
        }else if(realRes == 0 && imageRes < 0){
            result.getAng().getF().setValue(-Math.PI/2);
        }
        return result;
    }
    public static Vector sumAll(List<Vector> elem){
        Vector result = new Vector();
        result.getMag().getF().setValue(0.0);
        result.getAng().getF().setValue(0.0);
        for (Vector vector : elem) {
            result = sum(result, vector);
        }
        return result;
    }
}
