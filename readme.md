# 🌌 Gravity Simulation (Java + LWJGL)

A **3D educational project** that simulates gravitational interaction and orbital motion,
implemented in **Java using LWJGL (OpenGL)**.

This project was created for learning purposes to better understand:
- gravitational physics
- orbital mechanics
- numerical integration
- real-time rendering with OpenGL

---

## 🚀 Features

- 🌍 N-body gravitational simulation
- 🌀 Orbits velocity
- 📐 Symplectic Euler integration
- 🧲 Space-time curvature visualization (gravity grid)
- 🧵 Planet trajectory (orbit trail) rendering
- 🎥 Free camera movement
- 🖥️ Real-time OpenGL rendering

---

## 🛠️ Technologies

- **Java**
- **LWJGL 3**
- **OpenGL**
- **JOML** (vectors & matrices)
- **GLFW**

---

## 🎓 Project Purpose

This is a **learning-focused project** and does not aim for perfect physical accuracy.

Main goals:
- practice OpenGL and LWJGL
- understand gravity and orbital motion
- build a simple physics simulation engine
- improve low-level graphics and math skills

---

## 🙏 Inspiration & Acknowledgements

This project was inspired by:
- Simulating Gravity in C++  https://www.youtube.com/@kevkev-70
- open-source OpenGL & LWJGL examples
- developers and educators who share knowledge about physics simulations 
- textures pack usage  https://opengameart.org/content/planet-surface-textures

Special thanks to everyone contributing educational content to the community 💙

---

## 📄 License

This project is licensed under the **MIT License**.  
You are free to use, modify, and distribute the code with proper attribution.

---

## 👤 Author

**Danylo Diachenko**  
Software Engineer 

## 🌍 Usage 

IntelijIdea :
- open as project
- run

Without (Java 17 required) :
> in project root package
> 
> mvn clean package
>
> mvn exec:java

### Keyboard 
> Movement  - WASD - camera movement 
> 
> Z - Down X - Up
> 
> Rotation  - Hold right mouse button
>
> Alt + Q to set the BlackHole 10 point from camera (There is not simulation of it) 

You can change variables at core/util/Constants class

 
