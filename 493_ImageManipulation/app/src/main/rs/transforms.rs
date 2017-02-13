// File: singlesource.rs

#pragma version(1)
#pragma rs java_package_name(com.example.robin.imagemanipulation_493)


#define C_PI 3.141592653589793238462643383279502884197169399375
#include "rs_core.rsh"

static const float4 weight = {0.299f, 0.587f, 0.114f, 0.0f};
uint32_t height;
uint32_t width;
const uchar4* input;

static uchar4 getPixelAt(int, int);

uchar4 RS_KERNEL swirl(uchar4 in, uint32_t x, uint32_t y)
{

	int srcX, srcY;
	float relX, relY, cX, cY;
	float angle, new_angle, radius;

	cX = (float) width / 2.0f;
	cY = (float) height / 2.0f;
	relY = cY-y;

	relX = x - cX;
	if (relX != 0)
	{
		angle = atan( fabs(relY) / fabs(relX));
		if (relX > 0 && relY < 0) angle = 2.0f * C_PI - angle;
		else if (relX <= 0 && relY >= 0) angle = C_PI - angle;
		else if (relX <=0 && relY < 0) angle += C_PI;
	}
	else
	{
		if (relY >= 0) angle = 0.5f * C_PI;
		else angle = 1.5f * C_PI;
	}

    radius = sqrt( relX*relX + relY*relY);
	new_angle = angle + (.001f * radius);

	srcX = (int)(radius * cos(new_angle)+0.5f);
	srcY = (int)(radius * sin(new_angle)+0.5f);
	srcX += cX;
	srcY += cY;
	srcY = height - srcY;

	return getPixelAt(srcX, srcY);
}


void processSwirl(rs_allocation inputImage, rs_allocation outputImage) {
  width = rsAllocationGetDimX(inputImage);
  height = rsAllocationGetDimY(inputImage);
  rsForEach(swirl, inputImage, outputImage);
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


//a convenience method to clamp getting pixels into the image
static uchar4 getPixelAt(int x, int y) {
	if(y>=height) y = height-1;
	if(y<0) y = 0;
	if(x>=width) x = width-1;
	if(x<0) x = 0;
	return input[y*width + x];
}