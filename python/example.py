from scipy.spatial.distance import euclidean
from imutils import perspective
from imutils import contours
import numpy as np
import imutils
import cv2

# Function to show array of images (intermediate results)
def show_images(images):
    for i, img in enumerate(images):
        cv2.imshow("image_" + str(i), img)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

# Use image path
img_path = "../images/example.jpg"

# Read image and preprocess
image = cv2.imread(img_path)

# gray
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
show_images([gray])

# create a CLAHE object (Arguments are optional).
clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8,8))
claheApplied = clahe.apply(gray)
show_images([claheApplied])

# equalizeHist
#equalizeHist = cv2.equalizeHist(gray)
#show_images([equalizeHist])

# blur
BLUR_SIZE = 25
blur = cv2.GaussianBlur(claheApplied, (BLUR_SIZE, BLUR_SIZE), 0)
show_images([blur])

# canny
CANNY_THRESHOLD_1 = 25
CANNY_THRESHOLD_2 = 50
canny = cv2.Canny(blur, CANNY_THRESHOLD_1, CANNY_THRESHOLD_2)
show_images([canny])

# dilate
dilate = cv2.dilate(canny, None, iterations=5)
show_images([dilate])

# erode
erode = cv2.erode(dilate, None, iterations=5)
show_images([erode])

# Find contours
cnts = cv2.findContours(erode.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
cnts = imutils.grab_contours(cnts)

# Sort contours from left to right as leftmost contour is reference object
(cnts, _) = contours.sort_contours(cnts)

# Draw contours
cv2.drawContours(image, cnts, -1, (0,255,0), 3)
show_images([image])

# Draw remaining contours
for cnt in cnts:
    boundingRect = cv2.boundingRect(cnt)

    # Calculate area
    CONTOUR_AREA_MIN_THRESHOLD = 10000
    CONTOUR_AREA_MAX_THRESHOLD = 50000
    area = boundingRect[2] * boundingRect[3]
    if CONTOUR_AREA_MIN_THRESHOLD < area and area < CONTOUR_AREA_MAX_THRESHOLD:
        cv2.rectangle(image, boundingRect, (0, 0, 255), 2, -1, 0);

show_images([image])