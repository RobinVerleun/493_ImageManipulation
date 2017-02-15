// File: singlesource.rs

#pragma version(1)
#pragma rs java_package_name(com.example.robin.imagemanipulation_493)


#define C_PI 3.141592653589793238462643383279502884197169399375
#include "rs_core.rsh"

uint32_t height;
uint32_t width;
float icentreX, icentreY;
float radius, radius2;
const uchar4* input;

static uchar4 getPixelAt(int, int);

uchar4 RS_KERNEL water(uchar4 in, uint32_t x, uint32_t y)
{
	int srcX, srcY;
    float wavelength = 16;
    float amplitude = 10;
    float phase = 0;

    float dx = x - icentreX;
    float dy = y - icentreY;
    float dist2 = dx*dx + dy*dy;
    if(dist2 > radius2) {
        srcX = x;
        srcY = y;
    } else {
        float dist = (float)sqrt(dist2);
        float amount = amplitude * (float)sin((float)(dist / wavelength * (C_PI * 2.0f) - phase));
        amount *= (radius - dist) / radius;
        if( dist != 0 ) {
            amount *= wavelength / dist;
        }
        srcX = x + dx*amount;
        srcY = y + dy*amount;
    }

	return getPixelAt(srcX, srcY);
}

void processWater(rs_allocation inputImage, rs_allocation outputImage) {
  width = rsAllocationGetDimX(inputImage);
  height = rsAllocationGetDimY(inputImage);
  radius = width / 2.0f;
  radius2 = radius * radius;
  icentreX = width / 2.0f;
  icentreY = width / 2.0f;

  rsForEach(water, inputImage, outputImage);
}

uchar4 RS_KERNEL ripple(uchar4 in, uint32_t x, uint32_t y)
{
    int srcX, srcY;
    int xWavelength, yWavelength;
    float nx, ny, fx, fy;

    xWavelength = 10.0f;
    yWavelength = 10.0f;

    nx = (float)y / xWavelength;
    ny = (float)x / yWavelength;

    fx = (float)sin(nx);
    fy = (float)sin(ny);

    srcX = x + 10 * fx;
    srcY = y + 10 * fy;

    return getPixelAt(srcX, srcY);
}

void processRipple(rs_allocation inputImage, rs_allocation outputImage) {
    width = rsAllocationGetDimX(inputImage);
    height = rsAllocationGetDimY(inputImage);
    rsForEach(ripple, inputImage, outputImage);
}

uchar4 RS_KERNEL twirl(uchar4 in, uint32_t x, uint32_t y) {

    int srcX, srcY;
    float angle = 1;

    float dx = x - icentreX;
    float dy = y - icentreY;
    float dist = dx*dx + dy*dy;

    if(dist > radius2) {
        srcX = x;
        srcY = y;
    } else {
        dist = (float)sqrt(dist);
        float a = (float)atan2(dy, dx) + angle * (radius - dist) / radius;
        srcX = icentreX + dist*(float)cos(a);
        srcY = icentreY + dist*(float)sin(a);
    }

    return getPixelAt(srcX, srcY);
}

void processTwirl(rs_allocation inputImage, rs_allocation outputImage) {
    width = rsAllocationGetDimX(inputImage);
    height = rsAllocationGetDimY(inputImage);
    icentreX = width / 2;
    icentreY = height / 2;
    radius = width / 2;
    radius2 = radius * radius;
    rsForEach(twirl, inputImage, outputImage);
}

//a convenience method to clamp getting pixels into the image
static uchar4 getPixelAt(int x, int y) {
	if(y>=height) y = height-1;
	if(y<0) y = 0;
	if(x>=width) x = width-1;
	if(x<0) x = 0;
	return input[y*width + x];
}