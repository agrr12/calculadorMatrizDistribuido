package PJBL;

import java.io.Serializable;

public class MatrizPacote implements Serializable {
	
	MatrizObjeto m1;
	MatrizObjeto m2;
	MatrizObjeto resultado;
	int linhaInicio;
	int colunaInicio;
	int nrOperacoes;
	
	public MatrizPacote(MatrizObjeto m1, MatrizObjeto m2, MatrizObjeto resultado, int linhaInicio, int colunaInicio,
			int nrOperacoes) {
		this.m1 = m1;
		this.m2 = m2;
		this.resultado = resultado;
		this.linhaInicio = linhaInicio;
		this.colunaInicio = colunaInicio;
		this.nrOperacoes = nrOperacoes;
	}
	

	
}
