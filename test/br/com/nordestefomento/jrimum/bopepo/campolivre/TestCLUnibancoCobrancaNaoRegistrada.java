package br.com.nordestefomento.jrimum.bopepo.campolivre;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.nordestefomento.jrimum.bopepo.EnumBancos;
import br.com.nordestefomento.jrimum.domkee.bank.febraban.Agencia;
import br.com.nordestefomento.jrimum.domkee.bank.febraban.Carteira;
import br.com.nordestefomento.jrimum.domkee.bank.febraban.ContaBancaria;
import br.com.nordestefomento.jrimum.domkee.bank.febraban.EnumTipoCobranca;
import br.com.nordestefomento.jrimum.domkee.bank.febraban.NumeroDaConta;
import br.com.nordestefomento.jrimum.domkee.bank.febraban.Titulo;
import br.com.nordestefomento.jrimum.domkee.entity.Pessoa;

public class TestCLUnibancoCobrancaNaoRegistrada {

	/**
	 * String Campo Livre.
	 */
	private static String TEST_CASE = "5123456100112233445566777";

	private ICampoLivre campoLivre;

	private Titulo titulo;

	/**
	 * <p>
	 * Situação: precisamos emitir um título para um sacado com essas
	 * características:
	 * </p>
	 * <p>
	 * Banco: UNIBANCO – identificação 409<br />
	 * Moeda: Real – R$ - identificação 9<br />
	 * Vencimento: 31 de dezembro de 2001<br />
	 * Valor: R$1000,00<br />
	 * Código para transação CVT: 5 (cobrança sem registro – 7744-5)<br />
	 * Número de referência do cliente(NossoNúmero) : 112233445566777<br />
	 * Número de referência do cliente (Código Conta): 1234561<br />
	 * </p>
	 * <p>
	 * NÚMERO DE ORIGEM DO CÓDIGO DE BARRAS:
	 * </p>
	 * <p>
	 * 
	 * <pre>
	 * 409  9  2  1546  0000100000  5  1234561  00  112233445566777
	 * </pre>
	 * 
	 * </p>
	 * <p>
	 * LINHA DIGITÁVEL:
	 * </p>
	 * <p>
	 * 
	 * <pre>
	 * 40995.12347  56100.112236  34455.667773  2  15460000100000
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @throws Exception
	 * 
	 * @since 0.2
	 */

	@Before
	public void setUp() throws Exception {

		Pessoa sacado = new Pessoa();
		Pessoa cedente = new Pessoa();

		ContaBancaria contaBancaria = new ContaBancaria();
		contaBancaria.setBanco(EnumBancos.UNIBANCO.create());
		contaBancaria.setNumeroDaConta(new NumeroDaConta(123456, "1"));
		contaBancaria.setCarteira(new Carteira(123,
				EnumTipoCobranca.SEM_REGISTRO));
		contaBancaria.setAgencia(new Agencia(01234, '1'));

		titulo = new Titulo(contaBancaria, sacado, cedente);
		titulo.setNumeroDoDocumento("1234567");
		titulo.setNossoNumero("11223344556677");
		titulo.setDigitoDoNossoNumero("7");

		campoLivre = Factory4CampoLivre.create(titulo);
	}

	@Test
	public final void testGetInstance() {

		assertNotNull(campoLivre);
		assertTrue(campoLivre instanceof CLUnibancoCobrancaNaoRegistrada);
	}

	@Test(expected = CampoLivreException.class)
	public final void testGetInstanceComContaNula() {

		titulo.getContaBancaria().setNumeroDaConta(null);
		campoLivre = Factory4CampoLivre.create(titulo);
	}

	@Test(expected = CampoLivreException.class)
	public final void testGetInstanceComContaNegativa() {

		titulo.getContaBancaria().setNumeroDaConta(new NumeroDaConta(-23));
		campoLivre = Factory4CampoLivre.create(titulo);
	}

	@Test(expected = CampoLivreException.class)
	public final void testGetInstanceComDigitoDaContaNula() {

		titulo.getContaBancaria().setNumeroDaConta(new NumeroDaConta(23, null));
		campoLivre = Factory4CampoLivre.create(titulo);
	}

	@Test(expected = CampoLivreException.class)
	public final void testGetInstanceComDigitoDaContaNegativa() {

		titulo.getContaBancaria().setNumeroDaConta(new NumeroDaConta(2, "-3"));
		campoLivre = Factory4CampoLivre.create(titulo);
	}

	@Test(expected = CampoLivreException.class)
	public final void testGetInstanceComDigitoDaContaNaoNumerico() {

		titulo.getContaBancaria().setNumeroDaConta(new NumeroDaConta(-23, "X"));
		campoLivre = Factory4CampoLivre.create(titulo);
	}

	@Test(expected = CampoLivreException.class)
	public final void testGetInstanceComNossoNumeroNula() {

		titulo.setNossoNumero(null);
		campoLivre = Factory4CampoLivre.create(titulo);
	}

	@Test(expected = CampoLivreException.class)
	public final void testGetInstanceComNossoNumeroNegativo() {

		titulo.setNossoNumero("-012345679012345");
		campoLivre = Factory4CampoLivre.create(titulo);
	}

	@Test(expected = CampoLivreException.class)
	public final void testGetInstanceComNossoNumeroNaoNumerico() {

		titulo.setNossoNumero("123456790123y45");
		campoLivre = Factory4CampoLivre.create(titulo);
	}

	@Test
	public final void testITextStream() {

		assertEquals(TEST_CASE, campoLivre.write());

		campoLivre.read(TEST_CASE);

		assertEquals(TEST_CASE, campoLivre.write());
	}
}
