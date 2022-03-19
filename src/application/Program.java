package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.entities.Autenticacao;
import model.entities.Cliente;
import model.entities.Emprestimo;
import model.entities.Endereco;
import model.entities.Parcelas;
import model.exception.DomainException;
import model.services.ServicoEmprestimo;
import model.services.TaxaMensalEmprestimoTQI;
import model.util.DiferencaDatas;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dataSistema = new Date();

		Autenticacao autenticacao = new Autenticacao();
		Map<String, Long> auth = new HashMap<>(); // Declarado Map<> para l�gica de autentica��o.
		
		//Declara��o de vari�veis globais.
		boolean loop = true;
		Long codEmprestimo = 0L;
		Long idCliente = 0L;
		char resposta;
		Double renda;
		String email;

		try {
			while (loop) {
				System.out.println("********** Bem-vindo(a) ao servi�o de empr�stimos do TQIBank ********** "
						+ sdf2.format(dataSistema));
				System.out.println("---------- Desenvolvedor: Eliezer Moraes Silva ----------");
				System.out.println();
				System.out.print(
						"J� � cliente TQI?\nDigite 's' para acessar a �REA DO CLIENTE ou 'n' para CADASTRAR-SE: ");
				resposta = sc.next().charAt(0);
				sc.nextLine();
				if (resposta != 's' && resposta != 'n') {
					System.out.println("Resposta inv�lida. Digite 's' para SIM e 'n' para N�O.");
				}
				System.out.println();

				if (resposta == 'n') {
					System.out.println("********** Cadastro de novo cliente **********\n");
					System.out.print("1. Nome completo: ");
					String nome = sc.nextLine();
					System.out.print("2. CPF (somente n�meros): ");
					String cpf = sc.nextLine();
					System.out.print("3. RG (somente n�meros): ");
					String rg = sc.nextLine();
					System.out.print("4. Qual a sua renda mensal? (R$ 0000,00) R$ ");
					renda = sc.nextDouble();
					sc.nextLine();
					System.out.print("5. Digite o seu melhor e-mail: ");
					email = sc.nextLine();
					System.out.print("6. Digite uma senha segura (digite no m�nimo 8 d�gitos): ");
					String senha = sc.nextLine();
					if (senha.length() < 8) {
						throw new DomainException("Digite no m�nimo 8 d�gitos.");
					}
					System.out.println();

					// Verifica se o e-mail j� existe, ou seja, se j� foi cadastrado.
					if (auth.containsKey(email)) {
						throw new DomainException(
								"********** E-MAIL J� CADASTRADO! :(\n********** TENTE NOVAMENTE. **********\n");
					}

					System.out.println("Endere�o");
					System.out.print("1. Rua: ");
					String rua = sc.nextLine();
					System.out.print("2. N�mero: ");
					int numero = sc.nextInt();
					sc.nextLine();
					System.out.print("3. Bairro: ");
					String bairro = sc.nextLine();
					System.out.print("4. Cidade: ");
					String cidade = sc.nextLine();
					System.out.print("5. Estado: ");
					String estado = sc.nextLine();
					System.out.print("6. Pa�s: ");
					String pais = sc.nextLine();
					
					//Incrementar ID da classe cliente para uso futuro. Obter posi��o na lista de Clientes.
					idCliente++;
					//Instancia��o do objeto Cliente
					Cliente cliente = new Cliente(idCliente, nome, email.toUpperCase(), cpf, rg, renda, senha,
							new Endereco(rua, numero, bairro, cidade, estado, pais));
					//Adicionar o objeto Cliente na lista Cliente da classe Autentica��o.
					autenticacao.addClientes(cliente);
					
					//Adicionar os atributos email em letra ma�uscula para o login e ID cliente para relacionar a lista de clientes.
					auth.put(email.toUpperCase(), idCliente);

					System.out.println();
					System.out.println("********** CLIENTE CADASTRADO COM SUCESSO! :D **********\n");
					System.out.print("Deseja efetuar um empr�stimo?\nDigite 's' para SIM e 'n' para N�O: ");
					resposta = sc.next().charAt(0);
					sc.nextLine();
					System.out.println();

					while (resposta == 's') {
						System.out.println("********** �rea do cliente **********");
						System.out.println("********** Solicita��o de empr�stimo **********\n");
						System.out.println("---------- TAXA DE 2,77% am ----------\n");
						codEmprestimo++;
						int numeroDeParcelas = 0;
						boolean testeParcela = true;
						while (testeParcela) {
							System.out.print("Digite o n�mero de parcelas (somente n�meros): ");
							numeroDeParcelas = sc.nextInt();
							sc.nextLine();
							System.out.println();
							// L�gica da condi��o de quantidade m�xima de parcelas permitidas, conforme REGRA DE NEG�CIO.
							testeParcela = (numeroDeParcelas <= 60) ? false : true;
							if (numeroDeParcelas > 60) {
								System.out.println("N�mero de parcelas excedido. Parcelamento de no m�ximo 60 vezes.");
							}
						}
						System.out.print("Digite o valor do empr�stimo (R$ 0000,00): R$ ");
						double valorDoEmprestimo = sc.nextDouble();
						System.out.println();
						
						//Instancia��o de objeto para chamada de m�todo para c�lculo de datas. 
						DiferencaDatas data = new DiferencaDatas();
						System.out.print("Qual a data de vencimento da primeira parcela?\nDigite uma data entre "
								+ sdf.format(dataSistema) + " e " + sdf.format(data.dataLimite()) + ": ");
						Date dataMaximaParcela = sdf.parse(sc.next());
						System.out.println();

						// Classe utilit�ria para verificar a data da primeira parcela 3 meses ap�s a data atual, conforme REGRA DE NEG�CIO.
						data.diferencaDatas(dataMaximaParcela);

						// Sobrecarga para gerar a lista de parcelas
						Emprestimo emprestimoParcelas = new Emprestimo(valorDoEmprestimo, dataMaximaParcela);
						Emprestimo emprestimo = new Emprestimo(codEmprestimo, numeroDeParcelas, valorDoEmprestimo,
								dataMaximaParcela, renda, email);
						
						//Adicionar o objeto Empr�stimo na lista de empr�stimo da classe Cliente.
						cliente.addEmprestimo(emprestimo);
						
						//Camada de servi�o. Respons�vel pelos c�lculos do empr�stimo obedecendo as regras de n�gocio impostas.
						ServicoEmprestimo servicoEmprestimo = new ServicoEmprestimo(new TaxaMensalEmprestimoTQI());
						servicoEmprestimo.processarEmprestimo(emprestimoParcelas, numeroDeParcelas);

						// Imprimir lista de parcelas do empr�stimo efetuado
						System.out.println("********** EMPRESTIMO EFETUADO COM SUCESSO! **********\n");
						System.out.println("Parcelas a serem pagas: ");
						for (Parcelas p : emprestimoParcelas.getParcelas()) {
							System.out.println(p);
						}
						System.out.println();
						
						//Imprimir listagem de empr�stimos realizados utilizando forEach()
						System.out.println("********** LISTAGEM DE EMPR�STIMOS REALIZADOS **********\n");
						for (Emprestimo e : cliente.getEmprestimo()) {
							System.out.println(e);
						}
						System.out.println();

						System.out.print("Deseja efetuar um novo empr�stimo?\nDigite 's' para SIM e 'n' para N�O: ");
						resposta = sc.next().charAt(0);
						sc.nextLine();
					}
				//AUTENTICA��O
				} else if (resposta == 's') {
					System.out.println("********** �rea do cliente **********\n");
					System.out.print("LOGIN (e-mail): ");
					String emailLogin = sc.nextLine();
					System.out.println();
					System.out.print("SENHA: ");
					String senha = sc.nextLine();
					System.out.println();
					
					Cliente cliente = new Cliente(emailLogin.toUpperCase(), senha);

					long posicao = 0L;
					String chaveValor = emailLogin;
					
					//Verificar se na lista de clientes contem o e-mail e senha digitados
					if (autenticacao.getClientes().contains(cliente)) {

						System.out.println("********** ACESSO EFETUADO COM SUCESSO! **********\n");
						
						// Cria novo empr�stimo ou exibe a listagem de contrato de empr�stimos solicitados pelo cliente, conforme REGRA DE NEG�CIO
						System.out.println("Digite 'e' para efetuar um NOVO EMPR�STIMO ou 'v' para visualizar sua LISTA DE EMPR�STIMO(S): ");
						resposta = sc.next().charAt(0);
						sc.nextLine();
						System.out.println();
						while (loop) {
							if (resposta == 'e') {
								
								//Uso do Map() para obter o valor (posicao na lista) atrav�s da chave (que � o e-mail digitado)
								posicao = auth.get(chaveValor.toUpperCase()) - 1L;

								System.out.println("********** �rea do cliente **********");
								System.out.println("********** Solicita��o de empr�stimo **********\n");
								System.out.println("---------- TAXA DE 2,77% am ----------\n");
								codEmprestimo++;
								int numeroDeParcelas = 0;
								boolean testeParcela = true;
								while (testeParcela) {
									System.out.print("Digite o n�mero de parcelas (somente n�meros): ");
									numeroDeParcelas = sc.nextInt();
									sc.nextLine();

									// L�gica da condi��o de quantidade m�xima de parcelas permitidas, conforme REGRA DE NEG�CIO
									testeParcela = (numeroDeParcelas <= 60) ? false : true; //opera��o tern�ria
									if (numeroDeParcelas > 60) {
										System.out.println(
												"N�mero de parcelas excedido. Parcelamento de no m�ximo 60 vezes.");
									}
								}
								System.out.print("Digite o valor do empr�stimo (somente n�meros): R$ ");
								double valorDoEmprestimo = sc.nextDouble();

								DiferencaDatas data = new DiferencaDatas();
								System.out
										.print("Qual a data de vencimento da primeira parcela?\nDigite uma data entre "
												+ sdf.format(dataSistema) + " e " + sdf.format(data.dataLimite())
												+ ": ");
								Date dataMaximaParcela = sdf.parse(sc.next());

								// Classe utilit�ria para verificar a data da primeira parcela 3 meses ap�s a data atual, conforme REGRA DE NEG�CIO
								data.diferencaDatas(dataMaximaParcela);

								// Sobrecarga para gerar a lista de parcelas
								Emprestimo emprestimoParcelas = new Emprestimo(valorDoEmprestimo, dataMaximaParcela);
								Emprestimo emprestimo = new Emprestimo(codEmprestimo, numeroDeParcelas,
										valorDoEmprestimo, dataMaximaParcela,
										autenticacao.getClientes().get((int) posicao).getRenda(), // Navega��o utilizando a programa��o orientada a objetos a nosso favor
										autenticacao.getClientes().get((int) posicao).getEmail()); // Navega��o utilizando a programa��o orientada a objetos a nosso favor
								
								// Adicionar o novo empr�stimo para o cliente que est� logado!
								autenticacao.getClientes().get((int) posicao).addEmprestimo(emprestimo); // Navega��o utilizando a programa��o orientada a objetos a nosso favor
								
								// Uso de Interfaces para deixar facilitar a manuten��o do c�digo e permitir modifica��es futuras ou incluir novas taxas, se necess�rio
								ServicoEmprestimo servicoEmprestimo = new ServicoEmprestimo(
										new TaxaMensalEmprestimoTQI());
								servicoEmprestimo.processarEmprestimo(emprestimoParcelas, numeroDeParcelas);

								// Imprimir lista de parcelas do empr�stimo efetuado
								System.out.println("********** EMPRESTIMO EFETUADO COM SUCESSO! **********\n");
								System.out.println("Parcelas a serem pagas: ");
								for (Parcelas p : emprestimoParcelas.getParcelas()) {
									System.out.println(p);
								}
								System.out.println();

								System.out.print("Digite 'e' para efetuar um NOVO EMPR�STIMO ou 'v' para visualizar sua LISTA DE EMPR�STIMO(S): ");
								resposta = sc.next().charAt(0);
								sc.nextLine();
								System.out.println();

							} else if (resposta == 'v') {

								posicao = auth.get(chaveValor.toUpperCase()) - 1L;
								System.out.println();
								
								int posicaoDetalhe = 0;
								//forEach() para percorrer a lista Empr�stimo e imprimir os valores conforme a posi��o do cliente logado
								for (Emprestimo e : autenticacao.getClientes().get((int) posicao).getEmprestimo()) {
									System.out.println("POSI��O: " + posicaoDetalhe + " -----> " + e);
									posicaoDetalhe++;
								}
								System.out.println();
								
								//DETALHAMENTO DA LISTA DE EMPR�STIMO conforme selecionado pelo usu�rio. REGRA DE NEG�CIO.
								System.out.print("Para mais DETALHES, digite a POSI��O do contrato de empr�stimo: ");
								int cod = sc.nextInt();
								sc.nextLine();
								System.out.println();
								
								// Imprimir os detalhes do empr�stimo selecionado
								System.out.println(autenticacao.getClientes().get((int) posicao).getEmprestimo().get(cod));
								System.out.println("Renda: " + autenticacao.getClientes().get((int) posicao).getRenda());
								System.out.println("E-mail: " + autenticacao.getClientes().get((int) posicao).getEmail());
								
								// Sair do while(loop)
								loop = false;
							// Sair do while(loop) caso seja digitado caracteres inv�lidos
							} else if (resposta != 'e' && resposta != 'v') {
								System.out.println("DIGITO INV�LIDO! ");
								loop = false;
							}
						}
						// Entrar no loop da tela de login "�REA DO CLIENTE"
						loop = true;
					} else {
						// Mensagem para e-mail e/ou senha incorretos na tela de login
						System.out.println("********** ACESSO NEGADO! E-MAIL E/OU SENHA INCORRETOS. **********\n");
					}
				}
				System.out.println();
			}
		// TRATAMENTO DE EXCE��ES. Criado exce��o personalizada.
		} catch (ParseException e) {
			System.out.println("Formato de data incorreto! (Exemplo: 28/04/2022)");
		} catch (DomainException e) {
			System.out.println("Erro: " + e.getMessage());
		} catch (RuntimeException e) {
			System.out.println("Erro inesperado!");
		} finally {
			// Fechar o Scanner aberto no in�cio atrav�s do bloco Finally
			sc.close();
		}
	}
}