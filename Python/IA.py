import ollama
import socket

def start_server():
    Server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    Server_socket.bind(('localhost', 8080))
    Server_socket.listen(1)
    print("Servidor de chatbot inicializado correctamente en localhost")
    
    while True:
        Client_socket, Addr = Server_socket.accept()
        print(f"conexion establecida con {Addr}")
        
        Data = Client_socket.recv(1024).decode('utf-8')
        print(f"mensaje recibido: {Data}")
        
        Response = ""
        
        try:
            Stream = ollama.chat(model='llama3.1:8b', messages=[{'role': 'user', 'content': Data}], stream=True)
            
            for Chunk in Stream:
                Response += Chunk['message']['content']
            print(Response)
    
        except Exception as e:
            Response = f"Error al procesar la solicitud: {e}"
            
        Client_socket.send(Response.encode('utf-8'))
        Client_socket.close()

if __name__ == "__main__":
    start_server()