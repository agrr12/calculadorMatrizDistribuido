package PJBL;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Calculador1{
	
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
            ServerSocket server = new ServerSocket(6000);
            System.out.println("1| Aguardando uma conexão ...");
            Socket socket = server.accept();
            System.out.println("1| Aguardando um objeto ...");
            ObjectInputStream leitor = new ObjectInputStream(socket.getInputStream());
            MatrizPacote mp = (MatrizPacote) leitor.readObject();
            System.out.println("1 | Recebida a Matriz: ");
            
            //Extrai os dados do pacote
            m1 = mp.m1;
            m2 = mp.m2;
            mResposta = mp.resultado;
            nrOperacoes = mp.nrOperacoes;
            linhaInicio = mp.linhaInicio;
            colunaInicio = mp.colunaInicio;
            
            System.out.println("1 | Realizando Operações.");
            //Realiza a multiplicação
            multiplicarMatrizes();
            System.out.println("1 | Operações concluídas.");

            
            //preparar pacote de retorno --Apenas a mResposta importa aqui
            MatrizPacote pacoteResposta = new MatrizPacote(m1,m2,mResposta,0,0,0);
            System.out.println("1 | Enviando Pacote.");
            mResposta.show();
            ObjectOutputStream gravador = new ObjectOutputStream(socket.getOutputStream());
            gravador.writeObject(pacoteResposta);
            System.out.println("1 | Pacote enviado.");
            socket.close();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}
