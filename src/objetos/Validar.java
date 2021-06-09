/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetos;

/**
 *
 * @author 52341
 */
public class Validar {
    
    public static boolean validarClave(String clave){
        char c;
      
        if(clave.length() != 5 || clave.isEmpty())
            return false;
         
        for(int i = 0; i < clave.length(); i++){
            c = clave.charAt(i); 
            if(!(c >= '0' && c <= '9' || c >= 'A' && c <= 'Z'))
                return false; 
        }
        return true;
    }
   
    public static boolean validarCorreo(String correo){
        char c;
      
        if(correo.length() > 40)
            return false;
         
        for(int i = 0; i < correo.length(); i++){
            c = correo.charAt(i); 
            if(!(c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c == '_' || c == '.' || c == '@'))
                return false; 
        }
        return true;
    }
   
    public static boolean validarModelo(String mod){
        char c;

        if(mod.length() > 30 || mod.isEmpty())
            return false;

        for(int i = 0; i < mod.length(); i++){
            c = mod.charAt(i); 
            if(!(c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c == ' ' || c == '+'))
               return false; 
        }
        return true;
    }

    public static boolean validarDomicilio(String nombre){
        char c;

        if(nombre.length() > 30)
            return false;

        for(int i = 0; i < nombre.length(); i++){
            c = nombre.charAt(i); 
            if(!(c >= 'a' && c <= 'z' || c >= 'á' && c <= 'ú' || c == 'é' || c == ' ' ))
               return false; 
        }
        return true;
    }
    
    public static boolean validarNombre(String nombre){
        char c;

        if(nombre.length() > 30 || nombre.isEmpty())
            return false;

        for(int i = 0; i < nombre.length(); i++){
            c = nombre.charAt(i); 
            if(!(c >= 'a' && c <= 'z' || c >= 'á' && c <= 'ú' || c == 'é' || c == ' ' || c == '.'))
               return false; 
        }
        return true;
    }

    public static boolean validarCalle(String calle){
        char c;

        if(calle.length() > 30)
            return false;

        for(int i = 0; i < calle.length(); i++){
            c = calle.charAt(i); 
            if(!(c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'á' && c <= 'ú' || c == 'é' || c == ' ' || c == '.'))
               return false; 
        }
        return true;
    }

    public static boolean validarTelefono(String telef){
        char c;

        if(telef.length() != 10 && !telef.isEmpty())
            return false;

        for(int i = 0; i < telef.length(); i++){
            c = telef.charAt(i); 
            if(!(c >= '0' && c <= '9'))
               return false; 
        }
        return true;
    }

    public static boolean validarMarca(String marca, boolean nulo){
        char c;

        if(marca.length() > 20 || nulo && marca.isEmpty())
            return false;

        for(int i = 0; i < marca.length(); i++){
            c = marca.charAt(i); 
            if(!(c >= 'a' && c <= 'z' ))
               return false; 
        }
        return true;
    }

    public static boolean validarDesc(String desc){
        char c;
        int descuento;

        if(desc.length() > 3 || desc.isEmpty())
            return false;

        for(int i = 0; i < desc.length(); i++){
            c = desc.charAt(i);
            if(!(c >= '0' && c <= '9' ))
                return false; 
        }

        descuento = Integer.parseInt(desc);
        return !(descuento < 0 || descuento > 100);
    }

    public static boolean validarCant(String cant, int min, int max){
        char c;
        int cantidad;

        if(cant.length() > 5 || cant.isEmpty())
            return false;

        for(int i = 0; i < cant.length(); i++){
            c = cant.charAt(i);
            if(!(c >= '0' && c <= '9' ))
                return false; 
        }

        cantidad = Integer.parseInt(cant);
        return !(cantidad < min || cantidad > max);
    }

    public static boolean validarPrecio(String prec){
        char c;
        int n = 0, puntos = 0;
        double precio;
        
        if(prec.length() > 9 || prec.isEmpty() || prec.length() == 1 && prec.charAt(0) == '.')
           return false;

        for(int i = 0; i < prec.length(); i++){
            c = prec.charAt(i);
            if(!(c >= '0' && c <= '9' || c == '.'))
               return false;
            if(c == '.') {       
               puntos++;
               if(puntos != 1)
                   return false;
            }
            else
                if(puntos == 1){ // Cuenta los numeros que hay despues del punto
                    n++;
                    if(n > 2)   // Revisa no haya más de dos decimales
                       return false;  
                }
        }

        precio = Double.parseDouble(prec);
        return !(precio < 0 || precio > 999999.99);
    }
    
    public static boolean validarAnio(String a){
        char c;
        int anio;
        
        if(a.length() != 4 || a.isEmpty())
            return false;
        
        for(int i = 0; i < a.length(); i++){
            c = a.charAt(i);
            if(!(c >= '0' && c <= '9' ))
               return false; 
        }

        anio = Integer.parseInt(a); 
        return !(anio < 0); 
    }
   
    public static boolean validarMes(String m){
        char c;
        int mes;
        
        if(m.length() != 2 || m.isEmpty())
            return false;

        for(int i = 0; i < m.length(); i++){
            c = m.charAt(i);
            if(!(c >= '0' && c <= '9' ))
               return false; 
        }

        mes = Integer.parseInt(m); 
        return !(mes < 1 || mes > 12);   
    }
   
    public static boolean validarDia(String d, String m, String a){
        char c;
        int dia, mes, anio;
        
        if(d.length() != 2 || d.isEmpty())
            return false;

        for(int i = 0; i < d.length(); i++){
            c = d.charAt(i);
            if(!(c >= '0' && c <= '9' ))
               return false; 
        }

        dia = Integer.parseInt(d); 
        if(dia < 1)
            return false;
        else{
            mes = Integer.parseInt(m); 
            anio = Integer.parseInt(a); 
        }
         
        switch(mes){
            case 4: case 6: 
            case 9: case 11:
                return !(dia > 30);

            case 2:
                if(anioBisiesto(anio))
                   return !(dia > 29);
                else
                   return !(dia > 28);

            default:
                return !(dia > 31);             
        }
    }
   
    public static boolean anioBisiesto(int anio){
        if(anio % 4 == 0){
            if(anio % 100 == 0){
                return anio % 400 == 0;
            }
            else
               return true;
        }
        else
           return false;
    }
    
}
