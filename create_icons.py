#!/usr/bin/env python3
import os
from PIL import Image, ImageDraw

def create_simple_icon(size, color="#2196F3"):
    # Create a new image with transparent background
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Draw a simple laundry machine icon
    # Background circle
    margin = size // 10
    draw.ellipse([margin, margin, size-margin, size-margin], fill=color)

    # Inner circle (door)
    inner_margin = size // 4
    draw.ellipse([inner_margin, inner_margin, size-inner_margin, size-inner_margin],
                fill='white', outline=color, width=2)

    return img

# Create icons for different densities
densities = {
    'mdpi': 48,
    'hdpi': 72,
    'xhdpi': 96,
    'xxhdpi': 144,
    'xxxhdpi': 192
}

base_path = "app/src/main/res"

for density, size in densities.items():
    icon = create_simple_icon(size)
    icon_round = create_simple_icon(size)

    # Save regular icon
    icon.save(f"{base_path}/mipmap-{density}/ic_launcher.png")
    # Save round icon
    icon_round.save(f"{base_path}/mipmap-{density}/ic_launcher_round.png")

print("Icons created successfully!")