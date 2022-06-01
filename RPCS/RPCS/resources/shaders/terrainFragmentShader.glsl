#version 420 core

in vec2 passTextureCoord;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 outColor;

layout(binding=0) uniform sampler2D backgroundTexture;
layout(binding=1) uniform sampler2D rTexture;
layout(binding=2) uniform sampler2D gTexture;
layout(binding=3) uniform sampler2D bTexture;
layout(binding=4) uniform sampler2D blendMap;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

void main(){

	vec4 blendMapColor = texture(blendMap, passTextureCoord);
	
	float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec2 tileCoords = passTextureCoord * 100.0;
	vec4 backgroundTextureColor = texture(backgroundTexture, tileCoords) * backTextureAmount;
	vec4 rTextureColor = texture(rTexture, tileCoords) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture, tileCoords) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture, tileCoords) * blendMapColor.b;
	
	vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;
	

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float brightness = dot(unitNormal, unitLightVector);
	brightness = max(brightness, 0.2);
	vec3 diffuse = brightness * lightColor;
	
	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	
	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

	outColor = vec4(diffuse, 1.0) * totalColor + vec4(finalSpecular, 1.0);
	
}