package com.comze_instancelabs.minigamesapi;

/**
 * Supported versions of minecraft.
 * 
 * @author mepeisen
 */
public enum MinecraftVersionsType {
	Unknown(false), V1_7(true), V1_7_R1(true), V1_7_R2(true), V1_7_R3(true), V1_7_R4(true), V1_8(true), V1_8_R1(
			true), V1_8_R2(true), V1_9(true), V1_9_R1(true), V1_9_R2(true), V1_10(true), V1_10_R1(true);

	/**
	 * {@code true} if this version is still supported.
	 */
	private final boolean isSupported;

	/**
	 * Constructor to create a version.
	 * 
	 * @param supported
	 *            true for support.
	 */
	private MinecraftVersionsType(boolean supported) {
		this.isSupported = supported;
	}

	/**
	 * {@code true} if this version is still supported. return {@code true} if
	 * this version is still supported.
	 */
	public boolean isSupported() {
		return this.isSupported;
	}

	/**
	 * Checks if this version equals given version.
	 * 
	 * <p>
	 * Notice: Pseudo versions (V1_7) will match every V1_7_R* version.
	 * </p>
	 * 
	 * @param type
	 *            version to compare to.
	 * @return {@code true} if this version matches given version.
	 */
	public boolean isEqual(MinecraftVersionsType type) {
		switch (this) {
		case V1_10:
			return type == V1_10 || type == V1_10_R1;
		case V1_7:
			return type == V1_7 || type == V1_7_R1 || type == V1_7_R2 || type == V1_7_R3 || type == V1_7_R4;
		case V1_8:
			return type == V1_8 || type == V1_8_R1 || type == V1_8_R2;
		case V1_9:
			return type == V1_9 || type == V1_9_R1 || type == V1_9_R2;
		default:
			return type == this;
		}
	}

	/**
	 * Checks if this version is below given version.
	 * 
	 * <ul>
	 * <li>V1_7 will be below V1_8*.</li>
	 * <li>V1_7_R3 will be below V1_7_R4.</li>
	 * </ul>
	 * 
	 * @param type
	 *            version to compare to.
	 * @return {@code true} if this version matches given version.
	 */
	public boolean isBelow(MinecraftVersionsType type) {
		if (isEqual(type)) return false;
		return this.ordinal() < type.ordinal();
	}

	/**
	 * Checks if this version is after given version.
	 * 
	 * <ul>
	 * <li>V1_8 will be after V1_7*.</li>
	 * <li>V1_7_R4 will be after V1_7_R3.</li>
	 * </ul>
	 * 
	 * @param type
	 *            version to compare to.
	 * @return {@code true} if this version matches given version.
	 */
	public boolean isAfter(MinecraftVersionsType type) {
		if (isEqual(type)) return false;
		return this.ordinal() > type.ordinal();
	}

}
