package PJBL;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import TCP.Mensageiro;

public class Coordenador {
	
	static MatrizPacote[] p;
	static Semaphore mutex = new Semaphore(0);
	
    public static MatrizPacote[] criarPacotes(int linha1, int linha2, int nrCalculadores, MatrizObjeto m1, MatrizObjeto m2, MatrizObjeto mResposta) {
    	MatrizPacote[] pacotes = new MatrizPacote[4]; //Cria o vetor que abrigará os multiplicadores
		int calculosPorCalculador = (int)(linha1*linha2)/nrCalculadores;	//Determina quantos cálculos cada thread fará
		int sobraDeCalculos = (linha1*linha2)%nrCalculadores;
		int contador = -1; //Inicia o contador em -1 para pular, dentro do loop, a criação do primeiro multiplicador, que é criado fora do loop.
		int index = 0; //Index no vetor dos multiplicadores
		
		pacotes[index] = new MatrizPacote(m1,m2, mResposta, 0, 0, calculosPorCalculador); //Cria o 1o multiplicador
		for(int x=0; x<linha1; x++) {
			for(int y=0; y<linha2; y++) {
				contador+=1;
				if(contador==calculosPorCalculador) { //Se passou por um número de células igual ao de cálculos por thread
					contador=0; //zera o contador
					index+=1; //atualiza o index
					pacotes[index] = new MatrizPacote(m1,m2, mResposta, x, y, calculosPorCalculador); //inclui o novo multiplicador
					if(index==pacotes.length-1) { //Atalho. Termina a iteração se já determinou a última thread
						//Caso não seja possível dividir perfeitamente os cálculos, adiciona a sobra à última thread.
						pacotes[pacotes.length-1].nrOperacoes+=sobraDeCalculos; 
						return pacotes;
					}
				}
			}
		}
		return pacotes;
		
	}
	
	
	public static void main (String[] args) {
		
		try {
			int linha1 = 300;
			int coluna1 = 200;
			int linha2 = 200;
			int coluna2 = 300;
			int nrCalculadores = 4;



			//Gera 2 matrizes aleatórias
            MatrizObjeto m1 = new MatrizObjeto(linha1, coluna1);
            MatrizObjeto m2 = new MatrizObjeto(linha2, coluna2);
        	//m1.show();
        	//m2.show();

            MatrizObjeto mResposta = new MatrizObjeto(new double[coluna1][linha2], coluna1, linha2); //Matriz resposta
            mResposta.criarMatrizInfinita(); //Matriz com valores só de INFINITO representa uma matriz vazia


            
            System.out.println("Vamos enviar a Matriz: ");

         
            //Gera os soquetes de comunicação
            Socket socket1 = new Socket("localhost", 6000);
            Socket socket2 = new Socket("localhost", 7000);
            Socket socket3 = new Socket("localhost", 8000);
            Socket socket4 = new Socket("localhost", 9000);           
            
            //Criar os pacotes que serão enviados
            MatrizPacote[] pacotes = criarPacotes(linha1, linha2, nrCalculadores, m1, m2, mResposta);
            MatrizPacote pacote1 = pacotes[0];
            MatrizPacote pacote2 = pacotes[1];
            MatrizPacote pacote3 = pacotes[2];
            MatrizPacote pacote4 = pacotes[3];
                                        
            //Prepara os Streams
            ObjectOutputStream gravador1 = new ObjectOutputStream(socket1.getOutputStream());
            ObjectOutputStream gravador2 = new ObjectOutputStream(socket2.getOutputStream());
            ObjectOutputStream gravador3 = new ObjectOutputStream(socket3.getOutputStream());
            ObjectOutputStream gravador4 = new ObjectOutputStream(socket4.getOutputStream());
            

            Socket[] s = {socket1, socket2, socket3, socket4};
            
            p = new MatrizPacote[4];
            
            //envia os pacotes
            gravador1.writeObject(pacote1);
            gravador2.writeObject(pacote2);
            gravador3.writeObject(pacote3);
            gravador4.writeObject(pacote4);
            System.out.println("Enviado!");
            

            for(int i=0; i<4; i++) {
                ObjectInputStream leitor = new ObjectInputStream(s[i].getInputStream());
                System.out.println("Pacote do Calculador "+i+" Recebido");
    			p[i]=(MatrizPacote) leitor.readObject();
    			s[i].close();
            }
 

            
            int matrizAtual = 0;
        	for(int x=0; x<coluna1; x++) {
        		for(int y=0; y<linha2; y++) {
        			if(p[matrizAtual].resultado.m[x][y]==Double.POSITIVE_INFINITY) {
        				matrizAtual+=1;
        			}
        			mResposta.m[x][y]=p[matrizAtual].resultado.m[x][y];
        		}
        	}
        	
        	mResposta.show();

            
            


            System.out.println("Recebida a Matriz: ");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}

}
