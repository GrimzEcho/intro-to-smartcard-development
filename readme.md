# Introduction to Smart Card Development on the Desktop


## Table of Contents:
* [Introduction](#heading--introduction)
* [Requirements](#heading--requirements)
* [Resources](#heading--resources)
* [Definitions](#heading--definitions)
* [General Development](#heading--general-development)
   * [Smartcard Commands](#heading--commands)
   * [Smartcard Responses](#heading--responses)
   * [Answer to Reset (ATR)](#heading--atr)
* [Development Envrionments](#heading--development-envrionments)
   * [Windows](#heading--windows)
   * [Linux](#heading--linux)
   * [Java](#heading--java)
      * [Program - Example 1](#heading--java-example-1)
      * [Program - Example 2](#heading--java-example-2)
      * [Packaging in Java 9+](#heading--java-packaging)
      * [Java on Linux](#heading--java-linux)
   * [Python](#heading--python)
      * [Installing on Windows](#heading--install-windows)
      * [Installing on Linux](#heading--install-linux)
      * [Virutal Envrionments](#heading--virtual-env)
      * [Program - Example 1](#heading--python-example-1)
      * [Program - Example 2](#heading--python-example-2)
* [Standards and Specifications](#heading--standards)


#
<h1 id="heading--introduction">Introduction</h1>

This guide and the source files for the examples is available at:  
https://github.com/BrianHVB/intro-to-smartcard-development


This is a short guide (with accompanying source code) to help developers get started with reading, writing and programming smart cards. While the focus of this guide is the software, hardware, and tools necessary to work with various types of smartcards, the guide is geared for those wanting to interact with the various implantable NFC devices produced sold by Dangerous Things. Specifically, when writing this guide I focused on the xNT implant and the Fidesmo reference cards that are being used as a stand-in for the forthcoming implantable Vivokey.

While this guide will discuss several types of cards, it is not specific to any one and no card specific commands or configuration will be used. Instead, the aim of this guide is to get a generic environment set up that can then be further enhanced for either JavaCard development (Vivokey/Fidesmo), or Mifare development (nXT and similar)

The source code for this guide contains small but working examples of interfacing with smartcards in Python, Java, and Node. The examples *should* be cross-platform, but they were tested only on Windows 10 and Linux (Ubuntu). Mac users may have to mix-and-match the setup from the other two versions.

#
<h3 id="heading--requirements">Requirements</h3>

##### Hardware:
* A USB smartcard reader
* Several types of smartcards and/or NFC tags

For the reader, I recommend the ACR 122U manufactured by ACS. The one I use was [purchased from Amazon](https://www.amazon.com/gp/product/B01KEGQFYY/) for around $40, and I've been very happy with its performance. The reader has good range when reading from my implanted xNT, and I had no issues reading physical cards or NFC stickers. I also tested a [Feitian R502](https://www.ftsafe.com/onlinestore/product?id=13) contactless reader. The reader comes with some great documentation and an SDK, but the performance was awful when trying to read/write to an implant. It took 15+ minutes before I found an orientation that would allow it to read my xNT, and I was never able to get a strong enough coupling to write.

If you are planning on developing for the xNT or xM1 then it is imperative that you buy some inexpensive, non-plantable chips for practice and testing. I don't have much experience with the xM1, but I know it is easy to brick the xNT implant (or permanently write-lock it) with the wrong commands. The xNT uses a Mifare Ultralight NTAG 216, and there are lots of vendors selling these is a variety of shapes and materials. I purchased an [NTAG216 10 Pack - Metal Sticker](https://www.amazon.com/gp/product/B00TRL774A/) from Amazon for $10. The tags read cleanly from either USB reader and my phone.

Although this guide is focused on desktop development and hardware, I would also recommend having an NFC enabled smartphone. The NFC tools available for Android are generally superior to similar tools made for the desktop. When testing NFC devices, I use a Samsung Galaxy S8 and a ZTE Axion 7.

#### Software:

The choice of software depends on what language you want to develop in, and on what operating system you are using. The easiest combination is Java/Windows, followed closely by Python/Linux. Setting up Java/Linux or Python/Windows is a little more involved, but still easy. I have not finished a cross-platform Node setup, so I will save that for a follow-up.

**Drivers/SDK**: Both of the readers have associated SDKs, and both of the SDKs include drivers. However, none of the driver's need to be installed on either Linux or Windows. The Feitian driver is available from the product page (linked to above), but the ACR 122u SDK was not included with the purchase. It can be purchased directly from ACS, or you can find an existing copy online. There are several available, including [this one hosted on GitHub](https://github.com/derekneely/Code-Repo/tree/master/ACR122-SDK). I'm uncertain of the copyright legality, but since ACS typically releases the SDK to third party ACR 122u vendors (who in turn provide it to customers upon purchase), I believe it can be redistributed.

Additional requirements will be provided in the individual language-specific section.

#
<h1 id="heading--resources">List of Resources</h1>
Here are some of the resources I used frequently:

**ACR 122U**
* ACS 122U Contactless NRC reader Writer:  
  https://www.amazon.com/gp/product/B01KEGQFYY/

* ACR122U Drivers and Datasheets:  
  https://www.acs.com.hk/en/driver/3/acr122u-usb-nfc-reader/

* ACR122U SDK (unofficial - found on GitHub)  
  https://github.com/derekneely/Code-Repo/tree/master/ACR122-SDK

* ACR122U API Manual (good reference for basic APDU commands and structure)  
  https://www.acs.com.hk/download-manual/419/API-ACR122U-2.04.pdf

**NXP Datasheets**
* NTAG 216 Datasheet: The bible for working with the xNT or other NTAG 21x chips  
  https://dangerousthings.com/wp-content/uploads/NTAG213_215_216.pdf

**Python**
* pySCard - Opensource Python library for interacting with Smart Cards  
  https://pyscard.sourceforge.io/index.html  
  https://pyscard.sourceforge.io/user-guide.html

* PySCard Binaries/Installers - Useful for getting set up on Windows (Linux can use repositories)  
  https://ci.appveyor.com/project/LudovicRousseau/pyscard  

* virtualenvwrapper - tool to better manage the creation and activation of virtual environments  
  https://virtualenvwrapper.readthedocs.io/en/latest/index.html  

* Click - library for building command line apps in Python  
  http://click.pocoo.org/5/quickstart/

**Java**
* SmartCardIO - First-party Java library for interacting with Smart Cards  
  https://docs.oracle.com/javase/10/docs/api/java.smartcardio-summary.html

* String formatting - Why does Java have to be different  
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
<h1 id="heading--definitions">Definitions</h1>


| Term             | Definition                    |
| --------------- |:-----------------------------:|
| nibble | a 4-bit aggregation; a single hexadecimal character|
| byte   | an 8-bit aggregation; two hexadecimal characters |
| ICC | Integrated Circuit Card
| PICC | Proximity Integrated Circuit Card; e.g. the card or tag
| VICC | Vicinity Integrated Circuit Card - sometimes synonymous with PICC
| PCD | Proximity Coupling Device; e.g. a card reader
| IFD | Interface Device; e.g. a card reader
| NDEF | NFC Data Exchange Format; common data format for storing and transmitting data records
| PC/SC | Personal Computer/Smart Card; specification for smart-card integration into computing environments
| APDU | Application Protocol Data Units; commands to interact with ICCs
| T=0 | Communication protocol - byte transmission
| T=1 | Communication protocol - block transmission
| CCID | Chip Card Interface Device; protocol for smartcard communication via USB reader
| ATR | Answer to Reset; conveys information about communication parameters proposed by the card, and the card's nature and state.
| ATR | (alternate meaning); Attribute Request (ATR_REQ) and Attribute Response (ATR_RES)
| POR | Power-On Reset; Command or signal that first powers and activates a chip. A chip responds to a POR with its ATR
| CT | Cascade Tag (value 88) as defined in ISO 14443-3 Type A
| RFUI | Reserved for Future Use - Implemented - Designates a memory area that is not currently used


#
<h1 id="heading--general-development">General Development</h1>

Regardless of which programming language or libraries you choose to use, there is a standard set of commands, responses and procedures for interfacing with smartcards and smartcard readers. These standards come from a number of specifications, including [ECMA-340: Near Field Communication Interface and Protocol (NFCIP-1)](https://www.ecma-international.org/publications/standards/Ecma-340.htm), [ISO 7816: Smart Card Standard](https://en.wikipedia.org/wiki/ISO/IEC_7816), and the various specifications produced by the [PC/SC Working Group](https://www.pcscworkgroup.com/). For a more detailed list of useful specifications, see the standards section at the end of this guide.

#
<h3 id="heading--commands">Commands</h3>
Smartcard commands are referred to as APDUs, and all follow the same basic format. Commands are typically documented using hexadecimal notation, but most of the software libraries allow for them to be entered in other formats as well (decimal, binary, etc). Here is the basic command structure:

| CLA | INS|P1|P2|Data|
|:----------:|:----------:|:---------:|:---------:|:-----------:|
| Instruction Class| Instruction Code|Param 1| Param 2| Data|
| 1 byte  | 1 byte   | 1 byte   | 1 byte   | (max) 2^16 - 1 bytes  |



&nbsp;  
Here is an example command that will read the UID/serial number of most smartcards:

| NAME | CLA | INS | P1 | P2 | Data |
|:---------:|:----------:|:----------:|:---------:|:---------:|:-----------:|
| Get Data | FF | CA | 00 | 00 | 00 |

For this command, the data section is used to send the number by bytes to return. This is a common pattern, and the documentation often refers to this as L(e) for 'length expected'. It is typically the last byte(s) in the data section. To return all bytes of the UID, we send 00. If we knew which card we were targeting, and how long the UID was, we could have specified the number.

While all smartcard commands are supposed to follow this structure, many vendors (especially NXP) often shorten the command documentation to just &nbsp; `CODE | DATA`. Depending on the structure of the library, you may be able to use the command directly, or you might have to convert it to the standard APDU format.

In addition to commands to read/write data from the card, most card readers also have pseudo-APDU commands for interacting with the reader. These commands can be used to set things like LED colors, security features. Generally, these commands are specific to each type/brand of reader. Here is a command to make the ACR122u beep:

| NAME |CLA | INS|P1|P2|Data|
|:---------:|:----------:|:----------:|:---------:|:---------:|:-----------:|
| Beep | FF | 00 | 40 | 00 | 04 01 00 03 03 |

#
<h3 id="heading--responses">Responses</h3>
Like commands, responses (also called APDUs) have a standard format:

| DATA | SW1| SW2 |
|:----------:|:----------:|:---------:|
| Data | Status 1 | Status 2|
| (max) 2^16 - 1 bytes  | 1 byte   | 1 byte   |

The two response codes indicate the success or error of the response. Typically the SW1 is used for universal responses, while SW2 is for command or application specific responses. The documentation for individual commands will include the possible responses. A few general responses include:

| Response | SW1 | SW2 | Description |
|:--------:|:----:|:------:|:------:|
| Success   | 90 | 00 | Completed successfully |
| Error   | 63  | 00  | General failure   |
| Error | 6A | 81 | Function not supported |



The typical command-response loop for APDUs involves picking the command from a table, specifying the data and/or length, then checking the response for a `90 00` success.

#
<h3 id="heading--atr">Answer to Reset (ATR)</h3>

One other commonality worth mentioning is the very first response that a reader gets when powering a smart card. Since smart cards don't hold state when unpowered, every power activation is a form of reset, hence the term. The ATR encodes information about the smart card, specifying everything from the card vendor, to physical characteristics such as transmission rates and electrical timings. Most of this information is used by the firmware on a card reader, but smartcard programmers typically use the ATR to determine the type of the card the reader. A detailed breakdown of the ATR can be found at https://en.wikipedia.org/wiki/Answer_to_reset.

For our purposes, we are really interested in the *historical bytes* section of the ATR. This area is reserved for card manufacturers to include whatever information they believe is necessary for their card. Typically it includes the name of the vendor and card, as well as the amount of writable memory. When programming a smart card application, the first part of the program almost always involves reading the ATR to detect the type of card and then either ignore it or continue.

The ATR is normally read by the smart card library and thus no specific command needs to programmed to retrieve it.

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

#
<h1 id="heading--development-environments">Development Environments:</h1>

Desktop operating systems use the PC/SC specifications for communicating with a reader's firmware. Readers that are PC/SC compliant typically use the default smartcard driver installed in most operating systems. Both Windows and Apple operating systems have a full PC/SC compliant system library, while Linux makes use of a PC/SC Light library. To interface with a reader/smartcard, you need both the generic (or reader specific driver) as well as the PC/SC library.

These two pieces of software enable higher-level smartcard libraries in various languages to be built. There are smartcard libraries available for almost all of the major programming languages, including C/C++, C#, Java, Python, Node, and others. This guide will focus on two of the higher-level languages: Java and Python


#
<h2 id="heading--windows">Windows</h2>

On Windows, no setup is needed before the PC/SC libraries and reader can be used. When you connect the reader, Window should recognize it as a *Microsoft Usbccid Smartcard Reader* or something similar. Many readers, including the ACR122u, are equipped with an LED light. Once the reader has successfully communicated with the computer, the light(s) will turn on and indicate that no card is present. The status light will only engage if communication occurs, so the lack of any lights on the ACR122u means that there is an issue either with the driver or with the PC/SC library.

#
<h2 id="heading--linux">Linux</h2>

There are many versions of Linux. Some have the PC/SC Lite and related packages already installed, others do not. The following instructions should work for any Debian based system, but they have only been tested on Ubuntu 16.04, Ubuntu 18.04, and Mint.

Connect the reader via USB, and look for the presence of a status light indicating that the reader can communicate with the computer (see Windows section above). If the reader lights up, place an NFC tag on it and check if it beeps or changes color. If so, then you have the necessary libraries installed. If not, follow the steps below.

Install the following packages using `apt install <paclage>`:

* libusb-dev (USB development)
* pcscd (PC/SC daemon)
* libpcsclite1 (PC/SC lite library)
* libpcsclite-dev (PC/SC lite development library)

To test if Linux has recognized your reader, run `lsusb` and look for the name of your reader.

To test if the PC/SC lite interface is working run `pcsc_scan` and look in the output for the presence of the reader and/or card.

On some systems you may have to manually start the PC/SC daemon with `sudo service pcscd start`, on others, the service will automatically be started when the reader is accessed.

Other libraries that may be necessary if the above doesn't work(don't install unless you need to):
* libusb++
* libccid


<h2 id="heading--java">Java</h2>
Java has had built-in, first party support for smartcard interaction since Java 1.6. Compared with some of the other libraries, Java's javax.smartcardio is fairly basic, with only a few commonly used classes, and a dozen or so methods. This makes the library easy to learn and use, but at the expense of longer and more verbose applications. There are various third-party libraries that extend smardcardio to specific smart card implementations.

#
<h3 id="heading--java-example-1">Example 1</h3>
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

<h3 id="heading--java-example-2">Example 2</h3>
The javax.smartcardio package works at the byte level almost exclusively. However, most smartcard documentation (and programmers!) prefer working with hexadecimal values. Thus, when writing smartcard application in Java, a few helper functions are almost mandatory:

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
        String condensed = hex.replaceAll("[^\\da-fA-F]", "");
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
<h2 id="heading--java-packaging">Packaging and Java 9+:</h2>

The above code samples will compile and run normally on Java 1.6 - 1.8, but Java 9 made some big changes to how programs were packaged. One of those changes was dropping and/or moving most of the `javax` libraries (including `javax.smartcardio`) from the standard set of libraries pre-loaded in the JRE.

There are a number of solutions to this depending on how available you need the application to be to older versions of Java. One solution is to manually include the needed libraries, then package the application into a .jar file. Setting this up is beyond the scope of the guide, but if you are using IntelliJ, the process is largely automated.

Another solution is to make use of Java 9 modules, and the new `module-info` descriptor. This is the most future-proof solution and is the way that Java programs will likely be bundled going forward. The following example was tested on Java 10. It should also work on Java 9 and 11, but *won't* work on Java 8 or before.

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
Here we have a root folder that contains our module `examples`. Inside the module, we have a `module-info.java` file which describes what our module requires and what it exports. Within the module, we create a package called `smartcard` which is where we place source code from above.

The contents of the file are as follows:

```java
// module-info.java
module examples {
    requires java.smartcardio;
    requires java.xml.bind;
}
```
Here we specify that our module requires two other modules that are no longer part of the standard Java runtime. Note that we are specifying **modules** that live in the new *java.xxx* space. These modules contain the **packages** *javax.smartcardio* and *javax.xml.bind*

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

#
<h2 id="heading--java-linux">Java and Linux</h2>

The example source was generated in a Windows environment without issue. However, when running the application on Linux (specifically Ubuntu), the application was not able to discover the reader. The issue is that on some Java versions, the default location (path) that Java searches for the pc/sc library is incorrect. The issue can be resolved by specifying the correct location.

**Locate the correct path**:
```
find /usr/ -name libpcsclite.so.1
```

**Option 1: &nbsp; Specify the path when you run the Java program**
```
java --module-path bin -m examples/smartcard.Example1  -Dsun.security.smartcardio.library=/path/to/libpcsclite.so.1
```

**Option 2: &nbsp; Specify the path as an envriomental variable**
```
export JAVA_OPTS="-Dsun.security.smartcardio.library=/path/to/libpcsclite.so.1
```

**Option 3: &nbsp; Specify the path in Code**
(obtained in prt from https://stackoverflow.com/questions/12376257/accessing-javax-smartcardio-from-linux-64-bits)
```java
public void setPcscLocation() {
   try {
      String command[] = {"find", "/usr", "-name", "libpcsclite.so.1"};
      Process p = Runtime.getRuntime().exec(comm);

      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      while ((line = reader.readLine()) != null && !line.equals("")) {
          if (line.contains("libpcsclite.so.1")) {
          System.setProperty("sun.security.smartcardio.library",line);
              break;
          }

      }
      p.waitFor();

   } catch (Exception e) {
      e.printStackTrace();
   }
}
```


#
<h1 id="heading--python">Python</h1>

There are several Python libraries for interacting with smartcard and readers. We will be using *pyscard*, which contains both a higher level API and a low-level API for working directly with the C compiled pc/sc interface. Our focus will be on the higher-level API.

As a library, *pyscard* offers a higher-level interface than Java's *smartcardio*, with things like event handling, signal listeners, and extensive documentation. The downside to this is that it takes a little more effort to get all of the library dependencies installed. In particular, psycard requires a non-Python development tool called [SWIG](http://www.swig.org/) which helps connect Python with the C/C++ pc/sc binaries. The pyscard library also requires that a native C/C++ compiler be installed on the system. As such, simply doing a `pip install psycard` won't cut it.


#
<h2 id="heading--install-windows">Installing on Windows</h2>


#### Installing Python and Pip
First, make sure that you have both Python 3 and Pip installed. You can test this by running `python --version` and `pip --version` from the command line. This guide was made using Python 3.6 and Pip 10, but any 3.x version should work. If you don't have Python/Pip installed, then the recommended install method is to use the Windows package manager, [Chocolatey](https://chocolatey.org/).

You can install Chocolatey by running the following command from an *administrative command prompt*. Or you can download a standard installer from the website.
```
@"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
```

Once Chocolately is installed, run the following commands to install Python and Pip
```
choco install python3
choco install pip
```


#
#### Installing psycard and its dependencies

The first and easiest option is to use one of the pre-built binary installers available from https://ci.appveyor.com/project/LudovicRousseau/pyscard. From the site, select the job that matches your preferred Python version and architecture. For Windows 10 on a 64-bit processor, you would choose the Python 3.6.x, ARCH=64 options. Next, select the *ARTIFACTS* tab, and then select the .msi installer file. Executing this file will install the needed dependencies along with the library itself.

When installing on Windows, you have two options. First, you can manually set up the dependencies by installing SWIG and Visual C++ 10.0. After both are installed, you can run `pip install pyscard`.

#
<h2 id="heading--install-linux">Installation on Linux</h2>

Python and pip usually come pre-installed on Linux systems. However, these systems typically include both Python 2 and Python 3. On older systems, the command `python` will refer to the 2.x version, while `python3` will refer to the 3.x one. The same goes for pip. On some systems, there might be both a `pip` and a `pip3`. You can find out which version you have installed by running `python --version`, and you can see the install location with a `which python` command.

If you need to install python or pip, use the following commands:
```
sudo add-apt-repository ppa:deadsnakes/ppa
sudo apt-get update
sudo apt-get install python3.6    or     sudo apt-get install python3.7
sudo apt-get install python3-pip
```

Next, install SWIG if it is not already present
```
sudo apt-get install swig3.0
```

Finally, install the pyscard library using pip. The pip installer will also use GCC to build several needed C files.
```
sudo pip install pyscard
```

# 
<h2 id="heading--virtual-env">Python and Virtual Envrionments</h2>

The above instructions will install the *pyscard* library globally on your system, making it available to every Python project on your system. This is not the preferred method for Python development. Instead, I recommend that you set up a virtual environment for each Python project, and then from within the environment, install any needed Python dependencies and libraries. By using a virtual environment you can pin down a specific Python and library version. A virtual environment also makes your app much easier to distribute.

The easiest way to get started using Python virtual environments is with the *virtualEnvWrapper* extension set. Setting up the extensions is fairly easy, but it does require that you edit your `.bashrc` file on Linux, or your *system path* on Windows. Detailed instructions are available at https://virtualenvwrapper.readthedocs.io/en/latest/install.html

If you get an error when sourcing your updated `.bashrc`, you may need to update an environmental variable. First, get the location of your Python install with a `which python`. Then edit `~/.bashrc` and before the other virtualenvwrapper commands, add the following:
```
export VIRTUALENVWRAPPER_PYTHON=/path/to/python3
```

Once the wrapper is installed, you would then use the following commands to set up the environment:
```bash
# create a new directory for the project
mkdir -p /path/to/project    # no -p on Windows
cd /path/to/project

# create and activate the virtual environment
mkvirtualenv -a /path/to/project smartcard
workon smartcard
```

Now that the environment is active, install any needed dependencies or libraries using `pip`. These libraries will only be installed for the current project

```
pip install pyscard
```

#
## Using pyscard
The library has some very good documentation available at https://pyscard.sourceforge.io/user-guide.html#introduction. The following code mostly follows the example from their quick start guide.

<h3 id="heading--python-example-1">Example 1</h3>

```python
# example1
from smartcard.Exceptions import CardConnectionException, NoCardException
from smartcard.System import *
from smartcard import util


class MustBeEvenException(Exception):
    pass


if __name__ == '__main__':

    # get and print a list of readers attached to the system
    sc_readers = readers()
    print(sc_readers)

    # create a connection to the first reader
    first_reader = sc_readers[0]
    connection = first_reader.createConnection()

    # get ready for a command
    get_uid = util.toBytes("FF CA 00 00 00")
    alt_get_uid = [0xFF, 0xCA, 0x00, 0x00, 0x00] # alternative to using the helper

    try:
        # send the command and capture the response data and status
        connection.connect()
        data, sw1, sw2 = connection.transmit(get_uid)

        # print the response
        uid = util.toHexString(data)
        status = util.toHexString([sw1, sw2])
        print("UID = {}\tstatus = {}".format(uid, status))
    except NoCardException:
        print("ERROR: Card not present")

```

The above code is pretty self-explanatory and mirrors the code that was used in the Java example. One nice thing is that the library already includes several utility functions, so we don't need to make our own. For the command, we use the utility function `toBytes()` which converts a string of hexadecimal pairs (optionally separated by space) into an array. The alternative would be to use a list of integers, e.g. `[0xFF, 0xCA, ...]`

Next, we obtain a list of all of the attached card readers, connect to the first one, and the open a connection to the card. Once the connection is open, we can send commands and receive responses. All responses come in a three-tuple of `(data, sw1, sw2)`, and we use a utility function to pretty print these as a hex string.

#
<h2 id="heading--python-example-2">Example 2</h2>

One problem with the above workflow is that it assumes the card is present on the reader. But what if we want to wait until a card is activated, then run a command? This type of scenario is where *pyscard* really shines

```python
# example2

from smartcard.CardRequest import CardRequest
from smartcard.Exceptions import CardRequestTimeoutException
from smartcard.CardType import AnyCardType
from smartcard import util

WAIT_FOR_SECONDS = 5

if __name__ == '__main__':
    # respond to the insertion of any type of smart card
    card_type = AnyCardType()

    # create the request. Wait for up to x seconds for a card to be attached
    request = CardRequest(timeout=WAIT_FOR_SECONDS, cardType=card_type)

    # listen for the card
    service = None
    try:
        service = request.waitforcard()
    except CardRequestTimeoutException:
        print("ERROR: No card detected")
        exit(-1)

    # when a card is attached, open a connection
    conn = service.connection
    conn.connect()

    # get and print the ATR and UID of the card
    get_uid = util.toBytes("FF CA 00 00 00")
    print("ATR = {}".format(util.toHexString(conn.getATR())))
    data, sw1, sw2 = conn.transmit(get_uid)
    uid = util.toHexString(data)
    status = util.toHexString([sw1, sw2])
    print("UID = {}\tstatus = {}".format(uid, status))
 ```

 Here we use the pyscard library to create a wait-loop for detecting the card. In this example we want to respond to any card type, but for most applications, you will use the `ATRCardType()` class to specify the type of card to respond to. If the card is attached to the reader (or is already present) during the timeframe, the program continues. Otherwise, an exception is raised and the program halts.

 If we wanted to operate on cards sequentially (to program a batch of cards for instance), we could easily attach another event that would execute when a card was removed and restart the wait-loop for the next.



#
<h1 id="heading--standards">Standards and Specifications</h1>

The following is a list and description of various standards that smart cards, smart card readers, smartcard communications protocols are based on. Many of the ISO standards are not free, and are quite expensive to legitimately purchase. Most of the other standards are available at no cost

#
ISO 14443
Identification cards -- Contactless integrated circuit cards -- Proximity cards
* https://en.wikipedia.org/wiki/ISO/IEC_14443
* https://www.iso.org/standard/73599.html
* 4 Parts:
    - Part 1 - Physical Characteristics
    - Part 2 - Radio frequency power and signal interface
    - Part 3 - Initialization and anticollision
    - Part 4 - Transmission protocol
* Two types of cards, Type A and Type B
    - Both use same transmission protocol, but differ in modulation methods and coding schemes

#
ISO 18092 / ECMA-340
Near Field Communication Interface and Protocol-1 (NFCIP-1)
* https://en.wikipedia.org/wiki/Near-field_communication
* https://www.ecma-international.org/publications/standards/Ecma-340.htm (free)
* http://standards.iso.org/ittf/PubliclyAvailableStandards/c038578_ISO_IEC_18092_2004(E).zip (free)
* Section 12.4 details commands and structures

#
ISO 21481 / ECMA-352
Near Field Communication Interface and Protocol-1 (NFCIP-2)
* https://en.wikipedia.org/wiki/Near-field_communication
* https://www.ecma-international.org/publications/files/ECMA-ST/ECMA-352.pdf (free)
* This NFCIP-2 Standard specifies the mechanism to detect and select one of three communication modes (NFC, PCD, VCD)
* Not really applicable to programming NFC devices

#
ISO 7816
Smart Card Standard
* https://en.wikipedia.org/wiki/ISO/IEC_7816
* Large, 15 part standard for Smart Cards
* Several sections relevant for sending and processing commands
* Section 4 contains the syntax for APDU commands
* Sections 8 and 9 contain common commands
* Parts
    - 7816-1:2011 Part 1: Cards with contacts—Physical characteristics
    - 7816-2:2007 Part 2: Cards with contacts—Dimensions and location of the contacts
    - 7816-3:2006 Part 3: Cards with contacts—Electrical interface and transmission protocols
    - 7816-4:2013 Part 4: Organization, security and commands for interchange
    - 7816-5:2004 Part 5: Registration of application providers
    - 7816-6:2016 Part 6: Interindustry data elements for interchange
    - 7816-7:1999 Part 7: Interindustry commands for Structured Card Query Language (SCQL)
    - 7816-8:2016 Part 8: Commands and mechanisms for security operations
    - 7816-9:2017 Part 9: Commands for card management
    - 7816-10:1999 Part 10: Electronic signals and answer to reset for synchronous cards
    - 7816-11:2017 Part 11: Personal verification through biometric methods
    - 7816-12:2005 Part 12: Cards with contacts—USB electrical interface and operating procedures
    - 7816-13:2007 Part 13: Commands for application management in a multi-application environment
    - 7816-15:2016 Part 15: Cryptographic information application

#
PC/SC
The standard for integrating Smart Cards into computing environments
* https://www.pcscworkgroup.com/
* The Java Smart Card I/O API runs on top of the PC/SC Stack
* Tools and libraries:
    - PCSC Lite: PC/SC compliant API for Linux (https://pcsclite.apdu.fr/)
    - PCSC Tools: Linux tools for testing, reading, and sending PC/SC commands (http://ludovic.rousseau.free.fr/softwares/pcsc-tools/)
    - PS/SC SDK for Windows: by SpringCard (https://www.springcard.com/en/download/find/file/pcsc-sdk)

#
CCID (chip card interface device)
USB protocol that allows a smart card to be connected to a computer via a card reader
* https://en.wikipedia.org/wiki/CCID_(protocol)
* http://www.usb.org/developers/docs/devclass_docs/DWG_Smart-Card_CCID_Rev110.pdf

#
ATR - Answer to Reset
* https://en.wikipedia.org/wiki/CCID_(protocol)
* The ATR is provided when a card is first read. The historical section is vendor-dependent but it typically used to identify the type and capabilities of the card

#
NDEF - NFC Data Exchange Format
* https://nfc-forum.org/our-work/specifications-and-application-documents/
* https://nfc-forum.org/our-work/specifications-and-application-documents/specifications/record-type-definition-technical-specifications/
