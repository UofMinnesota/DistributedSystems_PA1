namespace java project1  // defines the namespace   
      
    typedef i32 int  //typedefs to get convenient names for your types  
      
    service SuperNodeService {  // defines the service to add two numbers  
            string Join(1:string IP, 2:int Port), //defines a method  
            bool PostJoin(1:string IP, 2:int Port), //defines a method  
            string GetNode(), 
    }  
