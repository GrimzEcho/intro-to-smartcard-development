## Introduction to Smart Card Development on the Desktop


### Table of Contents:

[[toc]]


Introduction
------------

This is a short guide (with accompanying source code) to help developers get started with reading, writing and programming smart cards. While the focus of this guide is the software, hardware, and tools necessary to work with various types of smartcards, the guide is geared for those wanting to interact with the various impmantable NFC devices produced sold by Dangerous Things. Specifically, when writing this guide I focused on the xNT implant, and the Fidesmo reference cards that are being used as a stand in for the forthcoming implantable Vivokey.

While this guide will discuss several types of cards, it is not specific to any one and no card specific commands or configuration will be used. Instead, the aim of this guide is to get a generic envrionment set up that can then be further enhanced for either JavaCard development (Vivokey/Fidesmo), or Mifare development (nXT and similar)

The source code for this guide contains small but working examples of interfacing with smartcards in Pyhon, Java, and Node. The examples *should* be cross-platform, but they were tested only on Windows 10 and Linux (Ubuntu). Mac users may have to mix-and-match the steup from the other two versions.

Requirements:
-------------

##### Hardware:
* A USB smartcard reader
* Several types of smartcards and/or NFC tags

For the reader, I recommend the ACR 122U manufactured by ACS. The one I use was [purchased from Amazon](https://www.amazon.com/gp/product/B01KEGQFYY/) for around $40, and I've been very happy with its performance. The reader has good range when reading from my implanted xNT, and I had no issues reading physical cards or NFC stickers. I also tested a [Feitian R502](https://www.ftsafe.com/onlinestore/product?id=13) contactless reader. The reader comes with some great documentation and an SDK, but the performance was awful when trying to read/write to an implant. It took 15+ miutes before I found an orientation that would allow it to read my xNT, and I was never able to get a strong enough coupling to write.

If you are planning on developing for the xNT or xM1 then it is imperitive that you buy some inexpensive, non-plantable chips for practice and testing. I don't have much experience with the xM1, but I know it is easy to brick the xNT implant (or permenently write-lock it) with the wrong commands. The xNT uses a Mifare Ultralight NTAG 216, and there are lots of vendors selling these is a variety of shapes and materials. I purchased a [NTAG216 10 Pack - Metal Sticker](https://www.amazon.com/gp/product/B00TRL774A/) from Amazon for $10. The tags read cleanly from either USB reader and my phone.

Although this guide is focused on desktop development and hardware, I would also recommend having an NFC enabled smartphone. The NFC tools available for Anroid are generally superior to similar tools made for the destkop. When testing NFC devices, I use a Samsung Galaxy S8 and a ZTE Axion 7.

#### Software:

The choice of software depends on what langauge you want to develop in, and on what operating system you are using. The easiest combination is Java/Windows, followed closely by Python/Linux. Setting up Java/Linux or Python/Windows is a little more involved, but still easy. I have not finished a cross-platform Node setup, so I will save that for a follow-up.

**Drivers/SDK**: Both of the readers have associated SDKs, and both of the SDKs include drivers. However, none of the driver's need to be installed on either Linux or Windows. The Feitian driver is available from the product page (linked to above), but the ACR 122u SDK was not included with the purchase. It can be purchased directly from ACS, or you can find an existing copy online. There are several available, including [this one hosted on GitHub](https://github.com/derekneely/Code-Repo/tree/master/ACR122-SDK). I'm uncertain of the copyright legality, but since ACS typcially releases the SDK to third party ACR 122u vendors (who in turn provide it to customers upon purchase), I believe it can be redistributed.

Additiional requirements will be provided in the individual langauge-specific section.

#
List of Resources
-----------------
Here are some of the resources I used frequently:

**ACR 122U**
* ACS 122U Contactless NRC reader Writer:
  https://www.amazon.com/gp/product/B01KEGQFYY/

* ACR122U Drivers and Datasheets:
	https://www.acs.com.hk/en/driver/3/acr122u-usb-nfc-reader/

* 	ACR122U SDK (unofficial - found on GitHub)
	https://github.com/derekneely/Code-Repo/tree/master/ACR122-SDK

* 	ACR122U API Manul (good reference for basic ADPU commands and structure)
	https://www.acs.com.hk/download-manual/419/API-ACR122U-2.04.pdf

**NXP Datasheets**
*  NTAG 216 Datasheet: The bible for working with the xNT or other NTAG 21x chips
	https://dangerousthings.com/wp-content/uploads/NTAG213_215_216.pdf

**Python**
* 	pySCard - Opensource Python library for interacting with Smart Cards
   https://pyscard.sourceforge.io/index.html
	https://pyscard.sourceforge.io/user-guide.html

* PySCard Binaries/Installers - Useful for getting setup on Windows (Linux can use repositories)
  https://ci.appveyor.com/project/LudovicRousseau/pyscard

* virtualenvwrapper - tool to better management, creation and activation of virutal envrionments
  https://virtualenvwrapper.readthedocs.io/en/latest/index.html

* Click - library for building command line apps in Python
  http://click.pocoo.org/5/quickstart/

**Java**
*  SmartCardIO - First-party Java library for interacting with Smart Cards
   https://docs.oracle.com/javase/10/docs/api/java.smartcardio-summary.html

*	String formatting - Why does Java have to be different
	https://docs.oracle.com/javase/10/docs/api/java/util/Formatter.html

**Node**
* NFC-PCSC - Cross platform Node.js library for interacting with SmartCards
  https://github.com/pokusew/nfc-pcsc

**Misc**
* SpringCard PC/SC SDK  Excellent set of tools and binary applications for working with NFC and SmartCards (including a script runner and NDEF writer). Lots of examples too.  (Windows only)
     https://www.springcard.com/en/download/find/file/pcsc-sdk

*  Spotify: Trance Playlist
  	https://open.spotify.com/user/brianhvb/playlist/3I8vna3LV8rUZ791at2b1f?si=_hYSpUFJSDeJRfoPjFU19w


#
Definitions:
------------

| Term             | Definition                    |
| --------------- |:-----------------------------:|
| nibble | a 4-bit aggregation; a single hexidecimal character|
| byte   | an 8-bit aggregation; two hexidecimal characters |
| ICC | Integrated Circuit Card
| PICC | Proximity Integrated Circuit Card; e.g. the card or tag
| VICC | Vicinity Integrated Circuit Card - somtimes synomous with PICC
| PCD | Proximity Coupling Device; e.g. a card reader
| IFD | Interface Device; e.g. a card reader
| NDEF | NFC Data Exchange Format; common data format for storing and transmitting data records
| PC/SC | Personal Computer/Smart Card; specification for smart-card integration into computing envrionments
| ADPU | Application Protocol Data Units; commands to interact with ICCs
| T=0 | Communication protocol - byte transmission
| T=1 | Communication protocol - block transmission
| CCID | Chip Card Interface Device; protocol for smartcard communication via USB reader
| ATR | Answer to Reset; conveys information about communication parameters proposed by the card, and the card's nature and state.
| ATR | (alternate meaning); Attribute Request (ATR_REQ) and Attribute Response (ATR_RES)
| POR | Power-On Reset; Command or signal that first powers and activates a chip. A chip responds to a POR with its ATR
| CT | Cascade Tag (value 88) as defined in ISO 14443-3 Type A
| RFUI | Reserved for Future Use - Implemented - Designates a memory area that is not currently used

#
General Development
-------------------

Regardless of which programming langague or libaries you choose to use, there is a standard set of commands, responses and procedures for interfacing with smartcards and smartcard readers. These standards come from a number of specifications, including [ECMA-340: Near Field Communication Interface and Protocol (NFCIP-1)](https://www.ecma-international.org/publications/standards/Ecma-340.htm), [ISO 7816: Smart Card Standard](https://en.wikipedia.org/wiki/ISO/IEC_7816), and the various specifications produced by the [PC/SC Working Group](https://www.pcscworkgroup.com/). For a more detailes list of useful specifications, see the standards section at the end of this guide.

###### Commands:
Smartcard commands are refered to as ADPUs, and all follow the same basic format. Commands are typically documented using hexidecimal notation, but most of the software libraries allow for them to be entered in other formats as well (decimal, binary, etc). Here is the basic command structure:

| CLA | INS|P1|P2|Data|
|:----------:|:----------:|:---------:|:---------:|:-----------:|
| Instruction Class| Instruction Code|Param 1| Param 2| Data|
| 1 byte  | 1 byte   | 1 byte   | 1 byte   | (max) 2^16 - 1 bytes  |

#
Here is an example command that will read the UID/serial number of most smartcards:
| NAME | CLA | INS | P1 | P2 | Data |
|:---------:|:----------:|:----------:|:---------:|:---------:|:-----------:|
| Get Data | FF | CA | 00 | 00 | 00 |

For this command, the data section is used to send the number by bytes to return. This is a common pattern, and the documention often refers to this as L(e) for 'length expected'. It is typically the last byte(s) in the data section. To return all bytes of the UID, we send 00. If we knew which card we were targeting, and how long the UID was, we could have specified the number.

While all smartcard commands are supposed to follow this strucutre, many vendors (especially NXP) often shorten the command documentaion to just &nbsp; `CODE | DATA`. Depending on the structure of the libary, you may be able to use the command directly, or you might have to convert it to the standard ADPU format.

In addition to commands to read/write data from the card, most card readers also have psuedo-ADPU commands for interacting with the reader. These commands can be used to set things like LED colors, security features. Generally, these commands are specific to each type/brand of reader. Here is a command to make the ACR122u beep:

| NAME |CLA | INS|P1|P2|Data|
|:---------:|:----------:|:----------:|:---------:|:---------:|:-----------:|
| Beep | FF | 00 | 40 | 00 | 04 01 00 03 03 |

###### Responses:
Like commands, responses (also called ADPUs) have a standard format:

| DATA | SW1| SW2 |
|:----------:|:----------:|:---------:|
| Data | Status 1 | Status 2|
| (max) 2^16 - 1 bytes  | 1 byte   | 1 byte   |

The two response codes indicate the success or error of the response. Typically the SW1 is used for universal responses, while SW2 is for command or application specific responses. The documentation for individual commands will include the possible responses. A few general responses includee:

| Response | SW1 | SW2 | Description |
|:--------:|:----:|:------:|:------:|
| Success   | 90 | 00 | Completed successfully |
| Error   | 63  | 00  | General failurer   |
| Error | 6A | 81 | Function not supported |



The typical command-response loop for ADPUs involves picking the command from a table, specifying the data and/or length, then checking the response for a `90 00` success.


###### Answer to Reset (ATR):
One other commaniality worth mentioning is the very first response that a reader gets when powering a smartcard. Since smartcards don't hold state when unpowered, every power activation is a form of reset, hence the term. The ATR encodes information about the smartcard, specifying everything from the card vendor, to physical characteristics such as transmission rates and electrial timings. Most of this information is used by the firmware on a card reader, but smartcard programmers typically use the ATR to determine the type of the card the reader. A detailed breakdown of the ATR can be found at https://en.wikipedia.org/wiki/Answer_to_reset.

For our purposes, we are really interested in the *historical byts* section of the ATR. This area is reserved for card manfuacturers to include whatever information they believe is necessary for thier card. Typically it includes the name of the vendor and card, as well as the amount of writable memory. When programming a smartcard application, the first part of the program almost always involes reading the ATR to detect the type of card and then either ignore it or continue.

The ATR is normally read by the smartcard library and thus no specific command needs to programmed to retreieve it.

Example:
```
Vendor (04 = NXP)
|
D5 43 00 00 04   04    02        01   00      13    03 90 00
                 |      |         |   |       |
+------------+------+---------+-----------+--------------+
| Chip       | Type | Subtype | Version   | Storage size |
+------------+------+---------+-----------+--------------+
```

Here is a table showing the relevant portions for various NXP NTAG chips:

| Chip       | Type | Subtype | Version   | Storage size |
|------------|:------:|:---------:|:-----------:|:--------------:|
| NTAG210    | 04 | 01    | 01 00 | 0B |
| NTAG212    | 04 | 01    | 01 00 | 0E |
| NTAG213    | 04 | 02    | 01 00 | 0F |
| NTAG213F   | 04 | 04    | 01 00 | 0F |
| NTAG215    | 04 | 02    | 01 00 | 11 |
| NTAG216    | 04 | 02    | 01 00 | 13 |
| NTAG216F   | 04 | 04    | 01 00 | 13 |
| | | | | |
| NT3H1101   | 04 | 02    | 01 01 | 13 |
| NT3H1101W0 | 04 | 05    | 02 01 | 13 |
| NT3H2111W0 | 04 | 05    | 02 02 | 13 |
| NT3H2101   | 04 | 02    | 01 01 | 15 |
| NT3H1201W0 | 04 | 05    | 02 01 | 15 |
| NT3H2211W0 | 04 | 05    | 02 02 | 15 |
| | | | |
| MF0UL1101  | 03 | 01    | 01 00 | 0B |
| MF0ULH1101 | 03 | 02    | 01 00 | 0B |
| MF0UL2101  | 03 | 01    | 01 00 | 0E |
| MF0ULH2101 | 03 | 02    | 01 00 | 0E |


Development Envrionments:
-------------------------
Desktop operating systems use the PC/SC specifications for communicating with a reader's firmware. Readers that are PC/SC compliant typically use the default smartcard driver installed in most operating systems. Both Windows and Apple operatins sytems have a full PC/SC compliant system librairy, while Linux makes use of a PC/SC Light library. To interface with a reader/smartcard, you need both the generic (or reader specific driver) as well as the PC/SC library.

These two pieces of software enable higher-leve smartcard libraries in various langauges to be built. There are smartcard libraries avaialbe for almost all of the major programming langauges, including C/C++, C#, Java, Python, Node, and others. This guide will focus on two of the higher-level langauges: Java and Python



### Java

Java has had built-in, first party support for smartcard interaction since Java 1.6. Compared with some of the other libaries, Java's javax.smartcardio is fairly basic, with only a few commonly used classes, and a dozen or so methods. This makes the library easy to learn and use, but at the expense of longer and more verbose applications. There are various third-party libraries that extend smardcardio to specific smartcard implementations.

Here is a basic program for connecting to a card reader and getting the UID of an attached smartcard:

```java
package examples;

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

```
#

The javax.smartcardio package works at the byte level almost exclusively. However, most smartcard documentation (and programmers!) prefer working with hexidecimal values. Thus, when writing smartcard application in Java, a few helper functions are almost mandatory:

```java
package examples;

import java.util.Arrays;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;

public class ConversionHelpers {

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Fast utility function from StackOverflow for converting a byte array to a hex string
	 * @param bytes an array of bytes
	 * @return a string of hexadecimal values
	 */
	public static String bytesToHex(byte[] bytes) {
		// https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}


	/**
	 * Returns a pretified version of a hex string
	 * @return a string with hex values separated by a colon
	 */
	public static String bytesToPrettyHex(byte[] bytes) {
		String hexString = bytesToHex(bytes);
		return hexPrettyPrint(hexString);
	}


	/**
	 * Returns a byte array by striping out any non-digits from a string, then attempting a conversion
	 * using javax.xml.bind.DatatypeConverter.parseHexBinary()
	 * @param hex a string containing a valid sequence of two-digit hex characters
	 * @return a byte array
	 */
	public static byte[] hexToBytes(String hex) {
		String condensed = hex.replaceAll("\\D", "");
		return parseHexBinary(condensed);
	}


	/**
	 * Same as previous, except that the input in an array of characters
	 * @param hex a String array
	 * @return a byte array
	 */
	public static byte[] hexToBytes(String[] hex) {
		return (hexToBytes(Arrays.toString(hex)));
	}


	public static String hexPrettyPrint(String hexString) {
		StringBuilder sb = new StringBuilder();
		char[] chars = hexString.toCharArray();
		for (int i = 0; i < hexString.length(); i++) {
			sb.append(chars[i]);
			if (i % 2 == 1 && i != hexString.length() - 1) {
				sb.append(':');
			}
		}

		return sb.toString();
	}

	public static String bytesPrettyPrint(byte[] bytes) {
		return Arrays.toString(bytes);
	}
}
```

#
Using the above helpers, we can now re-write our small example in a more compact and frienldy manner:

``` java
package examples;

import javax.smartcardio.*;
import java.util.List;
import static examples.ConversionHelpers.*;

public class Example2 {
   public static void main(String[] args) throws CardException {
      // ...

      byte[] instruction = hexToBytes("FF CA 00 00 00");
      CommandAPDU getUID = new CommandAPDU(instruction);

      ResponseAPDU response = channel.transmit(getUID);
		String uid = bytesToPrettyHex(response.getData());
		//String status = bytesToPrettyHex(new byte[] {(byte)response.getSW1(), (byte)response.getSW2()});
		System.out.printf("UID: %s\tResponse: %s", uid, status);
   }
}
```
#

##### Packaging and Java 9+

The above code samples will copmile and run normally on Java 1.6 - 1.8, but Java 9 made some big changes to how programs were packaged. One of those changes was dropping and/or moving most of the `javax` libraries (including `javax.smartcardio`) from the standard set of libaries pre-loaded in the JRE.

There are a number of solutions to this depending on how available you need the application to be to older versions of Java. One solution is to manually include the needed libraries, then package the application into a .jar file. Setting this up is beyond the scope of the guide, but if you are using IntelliJ, the process is largely automated.

Another solution is to make use of Java 9 modules, and the new `module-info` descriptor. This is the most future-proof solution, and is the way that Java programs will likely be bundled going forward. The following example was tested on Java 10. It should also work on Java 9 and 11, but *won't* work on Java 8 or before.

Folder Structure:
```
root
   + examples
      + smartcard
         - ConversionHelpers.java
         - Example1.java
         - Example2.java
   - module-info.java
```
Here we have a root folder that contains our moudle `examples`. Inside the module, we have a `module-info.java` file which describes what our module requires and what it exports. Within the module, we create a package called `smartcard` which is where we place source code from above.

The contents of the file are as follows:

```java
// module-info.java
module examples {
	requires java.smartcardio;
	requires java.xml.bind;
}
```
Here we specify that our moudle requires two other modules that are no longer part of the standard Java runtime. Note that we are specifying **moudles** that live in the new *java.xxx* space. Theese modules contain the **packages** *javax.smartcardio* and *javax.xml.bind*

```java
//ConversionHelpers.java
package smartcard;

import java.util.Arrays;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;

public class ConversionHelpers {
   // ...
}
```

```java
//Example1.java
package smartcard;

import javax.smartcardio.*;
import java.util.Arrays;
import java.util.List;

public class Example1A {
   // ...
}
```

```java
package smartcard;

import javax.smartcardio.*;
import java.util.List;

import static smartcard.ConversionHelpersA.bytesToPrettyHex;
import static smartcard.ConversionHelpersA.hexToBytes;

public class Example2 {
   // ...
}
```

#
To compile the project from the command line, run the following command from the *root* directory:
```
javac -d bin/examples examples/module-info.java examples/smartcard/*.java
```

This command is similar to how we would normally compile Java class files, except that we are also including the new module-info manifest.

You will likely get a compiler warning about java.xml.bind being deprecated and marked for removal. This isn't a big deal, as we are just using the package for hex conversion. If/when it is removed, we can write our own, or use another third-party package.

#
To run the examples, use the following command while in the *root* directory:
```
 java --module-path bin -m examples/smartcard.Example1
```


#### Linux
The example source was generated in a Windows envrionment without issue. However, when running the application on Linux (specifically Ubuntu), the application was not able to discover the reader. The issue is that the default location (path) that Java searches for the smartcard reader is incorrect. The issue can be resolved by specifying the correct location when running the program





<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
