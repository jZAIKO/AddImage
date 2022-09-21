[![](https://jitpack.io/v/jZAIKO/AddImage.svg)](https://jitpack.io/#jZAIKO/AddImage)

## AddImage
Simple mover, rotar, escalar imagen
> Step 1. Add the JitPack repository to your build file

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
  
  > Step 2. Add the dependency
```gradle
dependencies {
	        implementation 'com.github.jZAIKO:AddImage:1.0.6'
	}
  ```

## XML - Vista

```

```


## Metodos
> llamar método para añadir la imagen
```
addImagen(@NonNull Context context, Bitmap bitmap)
```
> llamar método para obtener el total de items añadidos
```
getSize()
```
> llamar método para eliminar todos los items añadidos
```
eliminarAllImagen()
```
> llamar método para eliminar uno por uno los items añadidos
```
eiminarImagen()
```
> establezca un nuevo color para el fondo
```
setColor(String newColor)
```

### License

```
   Copyright 2022 jZAIKO, Corp 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
  ```
