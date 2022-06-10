import java.security.SecureRandom;
import java.util.*;

public class Cipher{
        
        private byte[] leftSide = {};
        private byte[] rightSide = {};
        private byte[] key = new byte[128];

        Cipher(){
            SecureRandom ran = new SecureRandom();
            ran.nextBytes(key);
        }
    
    
    // Calls crypt function on message. Rejoins halves of the message after encryption
    // and encodes to readable characters using base64 encoding.
    public String encrypt(String message){
        crypt(message);
        byte[] cryptedMessage = new byte[leftSide.length+rightSide.length];
        System.arraycopy(leftSide, 0, cryptedMessage, 0, leftSide.length);
        System.arraycopy(rightSide, 0, cryptedMessage, leftSide.length, rightSide.length);
        return(Base64.getEncoder().encodeToString(cryptedMessage));
    }
    // Decodes from base 64 to binary. Runs binary through feistel pattern with crypt function.  
    // Then rejoins halves after decryption.
    public String decrypt(String message){
        System.out.println("Decrypting....");
        message = new String(Base64.getDecoder().decode(message));
        crypt(message);
        byte[] cryptedMessage = new byte[leftSide.length+rightSide.length];
        System.arraycopy(leftSide, 0, cryptedMessage, 0, leftSide.length);
        System.arraycopy(rightSide, 0, cryptedMessage, leftSide.length, rightSide.length);
        return new String(cryptedMessage);
    }

    // Takes message and splits it in half. Feeds halves to feistel pattern.
    // Flips sides after rounds of feistel pattern to finalize encryption/decryption
    // The same function is used for both encryption and decryption
    private void crypt(String message){
        leftSide = Arrays.copyOfRange(
            message.getBytes(), 0, (message.length()/2));
        rightSide = Arrays.copyOfRange(
            message.getBytes(), (message.length()/2), message.length());
        int index = 0;
        for(int i=0;i<8;i++){
            byte[]splitKey = new byte[16];
            System.arraycopy(key, index,splitKey, 0, (key.length/8));
            index+=8;
            encryptionRound(splitKey);
        }
        flip();  
    }
    // Based on feistel cipher structure.Splits message in half. Encrypts rightside 
    // by unique algorithm.
    // Runs encrypted right side and left side through exclusive-or gate to produce new left side.
    // Left side becomes unaffected right side. Right side becomes the new left side.
    // Repeat this pattern as desired...
    private void encryptionRound(byte[] splitKey){
        byte[] encryptedRight = encryptionAlg(new String(rightSide), splitKey);
        // XOr with left and encrypted right side
        for(int i =0; i<leftSide.length;i++) {
            encryptedRight[i] = (byte)(leftSide[i] ^ encryptedRight[i]);
        }
        leftSide = rightSide;
        rightSide = encryptedRight;
    }   

    //Flips left side and right side
    private void flip(){
        byte[] temp = leftSide;
        leftSide = rightSide;
        rightSide = temp;
    }
 
    /*Unique encryption algorithm. Multiplies each char by
    primenumber function below. Adds character from partial key. Then mods by 94 and adds 32.*/
    private byte[] encryptionAlg(String side, byte[] splitKey) {
        char[] chArray = new char[side.length()];
        char[] keyString = (Base64.getEncoder().encodeToString(splitKey)).toCharArray();
        
        for(int i=0; i<side.length();i++){
            char ch = side.charAt(i);
            int prime = primeNum(ch);
            
            ch = (char)(((prime*ch)+(keyString[ch%24])%96)+32);
        }

        return (new String(chArray).getBytes());
    }

    //Takes integer n and produces nth prime number greater than 999.
    private static int primeNum(int inputNum) {
        int count = 0;
        int j =999;
        while(count < inputNum) {
            boolean isPrime = true;
            for(int i=2;i<=Math.sqrt(j);i++){
                if(j%i==0){
                    isPrime=false;
                }
            }
            if(isPrime){
                count++;
            }
            j++;
        } 
        return j-1;
    }

}
