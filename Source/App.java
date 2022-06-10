import java.util.Scanner;


public class App {
    static Scanner sc = new Scanner(System.in);
    static Cipher primal = new Cipher();
    static String option = ""; 
    
    public static void main(String[] args) {
        System.out.println("************************************");
        System.out.println("   Welcome! To Primal Cipher Chat!");
        System.out.println("************************************");
        System.out.println("Encrypt and decrypt messages...\n");
    while(!option.equals("q")){
        prompt();
    }   
}


    private static void prompt(){
        System.out.print(
            "Enter \"Q\" to quit the program. \"E\" to encrypt a message. \"D\" to decrypt an encrypted message.\nWhat would you like to do? : ");
        option = sc.nextLine().toLowerCase();


        System.out.println();
        if(!option.equals("e") && !option.equals("d") && !option.equals("q")){
            System.out.println("That is not a valid option. Please try again.");
        } else{
            switch(option){
                case "e":
                    encryptMessage();
                    break;
                case "d":
                    decryptMessage();
                    break;
                default:
                    System.out.println("\n\nEnding session....\n");
            }
        }
    }
    

    private static void encryptMessage() {
        System.out.print("Please enter a message to encrypt: ");
        String message = sc.nextLine();
        if(isValidInput(message)){
            System.out.println("Your encrypted message: " + primal.encrypt(message));
            System.out.println();
        }
    }


    private static void decryptMessage(){
        System.out.print("Please enter a message to decrypt: ");
        String message = sc.nextLine();
        if(isValidInput(message)){
            try{
                System.out.println("Your decrypted message: " + primal.decrypt(message));
            System.out.println();
            } catch(IllegalArgumentException e){
                System.out.println("Error trying to decrypt this message. Enter a different message.");
            }
        }
    }


    private static boolean isValidInput(String message){
        if(message.length()<1){
            System.out.println("You've entered...nothing...\n");
            return false;
        } else{
            for(char b : message.toCharArray()){
            if(b<32 || b>126) {
                System.out.println("That is not a valid input! Please try again.\n");
                return false;
            }
        }
        return true;
    }
}

}
