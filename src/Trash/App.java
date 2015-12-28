package Trash;

public class App {
	
	 	
	
	
	public static void main(String args[]){
		
		
		if(args.length != 1){
			
			System.out.println("Incorrect number of arguments");
			System.exit(-1);
		}
		
		if(args[0].trim().toUpperCase().equals("SERVER")){
			
			MyServer ms = new MyServer();
			
			ms.startServer();
			
		}else{
			
			MyClient mc = new MyClient();
			
			mc.startClient();
			
		}
		
	}
}
