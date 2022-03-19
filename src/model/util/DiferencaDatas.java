package model.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import model.exception.DomainException;

public class DiferencaDatas {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public DiferencaDatas() {
	}

	public long diferencaDatas(Date dataMaximaParcela) {

		Date dataSistema = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dataSistema);
		cal.add(Calendar.MONTH, 3);
		dataSistema = cal.getTime(); 
		
		// Diferen�a entre a data digitada pelo cliente e a data atual acrescida de 3 meses, convertida pela l�gica abaixo para DIAS
		long dif = dataMaximaParcela.getTime() - dataSistema.getTime();
		TimeUnit time = TimeUnit.DAYS;
		long diferenca = time.convert(dif, TimeUnit.MILLISECONDS);

		if ((diferenca + 1) <= 1 && (diferenca + 1) >= -89) {			
			return diferenca + 1; // Soma-se 1 para compensar o 0. Os dias come�am com 0 na bibliteca TimeUnit
		} else {
			throw new DomainException("Data inv�lida. A data deve estar entre " + sdf.format(new Date()) + " e " + sdf.format(dataSistema) + ".");
		}
	}
	
	// M�todo/opera��o que indica a data limite para o vencimento da primeira parcela, conforme REGRA DE NEG�CIO
	public Date dataLimite() {
		
		Date dataSistema = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataSistema);
		cal.add(Calendar.MONTH, 3);
		return dataSistema = cal.getTime();
		
	}
}
