from smartcard.CardRequest import CardRequest
from smartcard.Exceptions import CardRequestTimeoutException
from smartcard.CardType import AnyCardType
from smartcard import util

WAIT_FOR_SECONDS = 30

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

