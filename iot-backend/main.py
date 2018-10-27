import RPi.GPIO as GPIO
import time
import datetime
import sys
from lib.hx711py.hx711 import HX711
import lib.RC522.MFRC522 as MFRC522
import signal
from firebase.firebase import FirebaseApplication, FirebaseAuthentication
import json

continue_reading = True

SECRET = 'g1dsW85Rvn9SxfJ4N1r95fnkoQbwtUTSjS9fiQ4C'
DSN = 'https://smartshop-4e831.firebaseio.com/'
EMAIL = 'io.satyamtg@gmail.com'
authentication = FirebaseAuthentication(SECRET,EMAIL, True, True)
firebase = FirebaseApplication(DSN, authentication)
cartno = 7412350101


def cleanAndExit():
    print "Cleaning..."
    GPIO.cleanup()
    print "Bye!"
    sys.exit()

# Capture SIGINT for cleanup when the script is aborted
def end_read(signal,frame):
    global continue_reading
    print "Ctrl+C captured, ending read."
    continue_reading = False
    GPIO.cleanup()
def additem(itemcode, cartno):
    itemname="/inventory/"+str(itemcode).zfill(8)+"/name"
    name=firebase.get(itemname, None)
    itemprice="/inventory/"+str(itemcode).zfill(8)+"/price"
    price=firebase.get(itemprice, None)
    qtypath="/"+str(cartno)+"/items/"+name+"/quantity"
    qty=firebase.get(qtypath, None)
    if(qty>=1):
        qty = qty+1
        totprice = qty*price
        putpath="/"+str(cartno)+"/items/"+name
        data={'name' :name,'price' :totprice, 'quantity' :qty}
        firebase.patch(putpath, data)
    else:
        qty=1
        data={'name' :name, 'price' :price, 'quantity' :qty}
        putpath="/"+str(cartno)+"/items/"+name
        firebase.patch(putpath, data)

# Hook the SIGINT
signal.signal(signal.SIGINT, end_read)

# Create an object of the class MFRC522

MIFAREReader = MFRC522.MFRC522()

# Welcome message
print "Press Ctrl-C to stop."
prevwt=0

# This loop keeps checking for chips. If one is near it will get the UID and authenticate
while continue_reading:

    # Scan for cards
    (status,TagType) = MIFAREReader.MFRC522_Request(MIFAREReader.PICC_REQIDL)

    # If a card is found
    if status == MIFAREReader.MI_OK:
        print "Card detected"

    # Get the UID of the card
    (status,uid) = MIFAREReader.MFRC522_Anticoll()

    # If we have the UID, continue
    if status == MIFAREReader.MI_OK:

        # Print UID
        print "Card read UID: %s,%s,%s,%s" % (uid[0], uid[1], uid[2], uid[3])

        # This is the default key for authentication
        key = [0xFF,0xFF,0xFF,0xFF,0xFF,0xFF]

        # Select the scanned tag
        MIFAREReader.MFRC522_SelectTag(uid)

        # Authenticate
        status = MIFAREReader.MFRC522_Auth(MIFAREReader.PICC_AUTHENT1A, 8, key, uid)

        # Check if authenticated
        if status == MIFAREReader.MI_OK:
            itemrawcode=MIFAREReader.MFRC522_Read(8)
            MIFAREReader.MFRC522_StopCrypto1()
            itemcode = "".join(itemrawcode)
            item="/inventory/"+str(itemcode).zfill(8)+"/weight"
            reqwt = prevwt + firebase.get(item, None)
            removedwt = prevwt - firebase.get(item, None)
            print reqwt
            GPIO.cleanup()
            hx = HX711(5, 6)
            hx.set_reading_format("LSB", "MSB")
            hx.set_reference_unit(920)
            timeout = 0

            hx.reset()
           # hx.tare()

            scanWt = 1
            while (scanWt):
                try:
                    # These three lines are usefull to debug wether to use MSB or LSB in the reading formats
                    # for the first parameter of "hx.set_reading_format("LSB", "MSB")".
                    # Comment the two lines "val = hx.get_weight(5)" and "print val" and uncomment the three lines to see what it prints.
                    #np_arr8_string = hx.get_np_arr8_string()
                    #binary_string = hx.get_binary_string()
                    #print binary_string + " " + np_arr8_string

                    # Prints the weight. Comment if you're debbuging the MSB and LSB issue.
                    val = hx.get_weight(5)
                    print val
                    print prevwt
                    if (val>=prevwt-4 and val<=prevwt+4):
                        timeout = timeout + 1
                        if(timeout==20):
                            scanWt = 0
                    elif (val<prevwt-4):
                        if (val>=removedwt-2 and val<=removedwt+2):
                            print "Removed"
                            scanWt = 0
                            prevwt = val
                        else:
                            print "Error"
                            scanWt = 0
                    else:
                        if (val>=reqwt-2 and val<=reqwt+2):
                            additem(00000001, cartno)
                            print "Added"
                            scanWt = 0
                            prevwt = val
                        else:
                            print "Error"
                            scanWt = 0
                    hx.power_down()
                    hx.power_up()
                    time.sleep(0.5)
                except (KeyboardInterrupt, SystemExit):
                    GPIO.cleanup()
        else:
            print "Authentication error"
