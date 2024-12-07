import argparse
import cv2
import numpy as np
import os

def center_image(image, canvas_width, canvas_height):
  # Get image dimensions
  image_height, image_width = image.shape[:2]

  # Calculate the top-left corner coordinates for centering
  x_offset = (canvas_width - image_width) // 2
  y_offset = (canvas_height - image_height) // 2

  # Create a canvas with the specified dimensions
  canvas = np.zeros((canvas_height, canvas_width, 4), dtype=np.uint8)

  # Place the image on the canvas
  canvas[y_offset:y_offset+image_height, x_offset:x_offset+image_width] = image

  return canvas

if __name__ == "__main__":
  parser = argparse.ArgumentParser(description="Make a relic image!")
  parser.add_argument("--input", help="Path to input image", required=True)
  parser.add_argument("--name", help="Name of the relic to write.")
  args = parser.parse_args()

  if not os.path.isfile(args.input):
    print(f"Can't find file {args.input}")
    exit()

  im = cv2.imread(args.input, cv2.IMREAD_UNCHANGED)
  im = cv2.resize(im, (64, 64))
  center = center_image(im, 128, 128)
  cv2.imwrite(f"{args.name}.png", center)

  gray = cv2.cvtColor(center, cv2.COLOR_BGR2GRAY)
  ret, thresh = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY)
  final = cv2.merge((thresh, thresh, thresh, center[:, :, 3]))
  cv2.imwrite(f"outline/{args.name}.png", final)
