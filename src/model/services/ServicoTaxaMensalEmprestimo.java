package model.services;

// INTERFACE para c�lculo do juros mensal. Facilita a manuten��o do c�digo e permite implementa��o de outros valores e tipos de juros, se necess�rio no futuro.
public interface ServicoTaxaMensalEmprestimo {
	
	double taxaDeJurosMensal(double valorParcela);

}
