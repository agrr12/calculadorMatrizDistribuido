package PJBL;

import java.io.Serializable;
import java.util.Random;

public class MatrizObjeto implements Serializable  {
	public double [] [] m;
    public int linhas;
    public int colunas;

    public MatrizObjeto(double[][] m, int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.m = m;
    }
    
    public MatrizObjeto(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.m = criarMatriz(linhas, colunas);
    }

	public double[][] criarMatriz(int linhas, int colunas) {
		Random r = new Random();
		double[][] matriz = new double[linhas][colunas]; //Cria a matriz
		for(int x=0; x<linhas; x++) { //percorre a matriz
			for(int y=0; y<colunas; y++) {
				double random = r.nextDouble();
				double result = 0.0 + (random * (10.0));
				matriz[x][y]= result;
			}
		}
		return matriz;
	}
	
	public void criarMatrizInfinita(){
		double[][] matriz = new double[linhas][colunas]; //Cria a matriz
		for(int x=0; x<linhas; x++) { //percorre a matriz
			for(int y=0; y<colunas; y++) {
				m[x][y]= Double.POSITIVE_INFINITY;
			}
		}
		
	}
    
    public void show() {
        System.out.println("linhas : " + linhas);
        System.out.println("colunas: " + colunas);
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print( m[i][j] );
                System.out.print( " " );
            }
            System.out.println();
        }
    }
}
