package smartcard;

import javax.smartcardio.*;
import java.util.Arrays;
import java.util.List;


public class Example1 {

	public static void main(String[] args) throws CardException {
		// get and print any card readers (terminals)
		TerminalFactory factory = TerminalFactory.getDefault();
		List<CardTerminal> terminals = factory.terminals().list();
		System.out.println("Terminals: " + terminals);

		// work with the first terminal
		CardTerminal term = terminals.get(0);

		// connect with the card. Throw an exception if a card isn't present
		// the * means use any available protocol
		Card card = term.connect("*");
		System.out.println("card: " + card);

		// Once we have the card, we can open a communication channel for sending commands and getting responses
		CardChannel channel = card.getBasicChannel();

		// Create the command for reading the UID - "FF CA 00 00 00"
		// The smartcardio library wants a byte array. Bytes in java are signed numbers with a decimal value
		// between -128 to 127. So if we want to use the hex codes from the documentation, we need to cast.
		byte[] instruction = {(byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00, (byte)0x00};
		CommandAPDU getUID = new CommandAPDU(instruction);

		// Send the command and print the response
		// The response also comes as a byte array. If we want it to match standard format, we need to convert back to hex
		// The first x bytes will be the UID, and the last 2 bytes will be SW1 and SW2.
		// Success = [-112, 0] = [0x90, 0x00]
		ResponseAPDU response = channel.transmit(getUID);
		byte[] responseBytes = response.getBytes();
		System.out.println("response: " + Arrays.toString(responseBytes));


	}

}
