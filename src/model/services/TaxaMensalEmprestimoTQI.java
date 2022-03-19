package model.services;

// Adicionei uma taxa de empr�stimo mensal para o programa ficar mais realista e pr�ximo da realidade
// Classe TaxaMensalEmprestimoTQI implementa (CUMPRIR CONTRATO) da Interface ServicoTaxaMensalEmprestimo
public class TaxaMensalEmprestimoTQI implements ServicoTaxaMensalEmprestimo {
	
	private static final double PORCENTAGEM_JUROS_MENSAL = 0.0277; // 2.70% am

	@Override
	public double taxaDeJurosMensal(double valorParcela) {
		return valorParcela * PORCENTAGEM_JUROS_MENSAL;
	}
}
