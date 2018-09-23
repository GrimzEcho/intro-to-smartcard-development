package smartcard;

import javax.smartcardio.*;
import java.util.List;

import static smartcard.ConversionHelpers.bytesToPrettyHex;
import static smartcard.ConversionHelpers.hexToBytes;


public class Example2 {

	public static void main(String[] args) throws CardException {
		TerminalFactory factory = TerminalFactory.getDefault();
		List<CardTerminal> terminals = factory.terminals().list();
		System.out.println("Terminals: " + terminals);

		CardTerminal term = terminals.get(0);

		Card card = term.connect("*");
		System.out.println("card: " + card);

		CardChannel channel = card.getBasicChannel();

		byte[] instruction = hexToBytes("FF CA 00 00 00");
		CommandAPDU getUID = new CommandAPDU(instruction);

		ResponseAPDU response = channel.transmit(getUID);
		String uid = bytesToPrettyHex(response.getData());
		String status = bytesToPrettyHex(new byte[] {(byte)response.getSW1(), (byte)response.getSW2()});
		System.out.printf("UID: %s\tResponse: %s", uid, status);

	}
}
