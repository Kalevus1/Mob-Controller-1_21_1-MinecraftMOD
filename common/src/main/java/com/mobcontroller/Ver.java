package com.mobcontroller;

/**
 * Nivel de versión reconstruida (para regenerar los jars históricos de cada release).
 * Cada compilación fija este número y las funciones se activan a partir de su versión:
 *
 *   1 = 0.1.0  POV (cámara en los ojos, sin control)
 *   2 = 0.2.0  control WASD/ratón
 *   3 = 0.3.0  movimiento manual (mob.move) + cancelar clic
 *   4 = 0.4.0  vuelo/nado, atacar siendo el mob, pose (R)
 *   5 = 0.5.0  montura invisible (movimiento suave, salir junto al animal)
 *   6 = 0.6.2  cuerpo visible en 1ª persona (humanoides)
 *   7 = 0.7.0  cámara instantánea (giro sin desfase)
 *   8 = 0.8.0  patas de cuadrúpedos/lobo, el oso se yergue, sonidos de pose
 *   9 = 0.9.4  cámara en la cabeza (head.z), nado para todos, alineado instantáneo, sonido de ataque
 *  10 = 1.0.0  HUD de posesión (vida, hambre, aire)
 */
public final class Ver {
	private Ver() {
	}

	public static final int LEVEL = 10;
}
