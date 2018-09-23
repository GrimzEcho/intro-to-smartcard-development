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

