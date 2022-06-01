#version 400 core

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 passTextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec3 lightPosition;

void main(){

	vec4 worldPosition = model * vec4(position, 1.0);
	gl_Position = projection * view * worldPosition;
	passTextureCoord = textureCoord;
	
	surfaceNormal = (model * vec4(normal, 0.0)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
	toCameraVector = (inverse(view) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
}