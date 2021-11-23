package PJBL;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Calculador2{
	
	private static MatrizObjeto m1;
	private static MatrizObjeto m2;
	private static MatrizObjeto mResposta;
	private static int nrOperacoes;
	private static int linhaInicio;
	private static int colunaInicio;


	
	static void multiplicarMatrizes() {
		
		int contadorOperacoes = 0;
	    for (int linha = linhaInicio; linha < mResposta.linhas; linha++) {
	        for (int coluna = colunaInicio; coluna < mResposta.colunas; coluna++) {
	        	mResposta.m[linha][coluna] = mutplicarCelulaMatriz(linha, coluna);
	        	contadorOperacoes+=1;
	        	if(contadorOperacoes==nrOperacoes){return;}
	        	if(coluna==mResposta.colunas-1) {
	        		colunaInicio=0;
	        	}
	        }
	    }
	}
	
	static double  mutplicarCelulaMatriz(int linha, int coluna) {
	    double celula = 0;
	    for (int i = 0; i < m2.linhas; i++) {
	    	celula += m1.m[linha][i] * m2.m[i][coluna];
	    }
	    return celula;
	}
	
	
	public static void main( String[] args) {
        try {
        	//Setup para receber o Pacote Matriz
            ServerSocket server = new ServerSocket(7000);
            System.out.println("2| Aguardando uma conex�o ...");
            Socket socket = server.accept();
            System.out.println("2| Aguardando um objeto ...");
            ObjectInputStream leitor = new ObjectInputStream(socket.getInputStream());
            MatrizPacote mp = (MatrizPacote) leitor.readObject();
            System.out.println("2 | Recebida a Matriz: ");
            
            //Extrai os dados do pacote
            m1 = mp.m1;
            m2 = mp.m2;
            mResposta = mp.resultado;
            nrOperacoes = mp.nrOperacoes;
            linhaInicio = mp.linhaInicio;
            colunaInicio = mp.colunaInicio;
            
            System.out.println("2 | Realizando Opera��es.");
            //Realiza a multiplica��o
            multiplicarMatrizes();
            System.out.println("2 | Opera��es conclu�das.");

            
            //preparar pacote de retorno --Apenas a mResposta importa aqui
            MatrizPacote pacoteResposta = new MatrizPacote(m1,m2,mResposta,0,0,0);
            
            System.out.println("4 | Enviando Pacote.");
            mResposta.show();
            ObjectOutputStream gravador = new ObjectOutputStream(socket.getOutputStream());
            gravador.writeObject(pacoteResposta);
            System.out.println("4 | Pacote enviado.");
            socket.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}
