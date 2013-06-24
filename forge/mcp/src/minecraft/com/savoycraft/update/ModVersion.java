/**
 * Copyright (c) 2013 William Karnavas 
 * All Rights Reserved
 * 
 * This file is part of SavoyCraft.
 * 
 * SavoyCraft is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SavoyCraft is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SavoyCraft. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.savoycraft.update;

/**
 * Versions are written out as major.feature.bugfix.build, integers only in each
 * place. Version strings may write out anywhere from two to four numbers.
 * 
 * @author BJ
 * 
 */
public class ModVersion {

	public static final ModVersion CURRENT_VERSION = new ModVersion(0, 1, 0, 0);

	private int featureVersion;
	private int majorVersion;
	private int bugfixVersion;
	private int buildVersion;

	public ModVersion() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param featureVersion
	 * @param majorVersion
	 * @param bugfixVersion
	 * @param buildVersion
	 */
	public ModVersion(int featureVersion, int majorVersion, int bugfixVersion,
			int buildVersion) {
		super();
		this.featureVersion = featureVersion;
		this.majorVersion = majorVersion;
		this.bugfixVersion = bugfixVersion;
		this.buildVersion = buildVersion;
	}

	public int getFeatureVersion() {
		return featureVersion;
	}

	public void setFeatureVersion(int featureVersion) {
		this.featureVersion = featureVersion;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}

	public int getBugfixVersion() {
		return bugfixVersion;
	}

	public void setBugfixVersion(int bugfixVersion) {
		this.bugfixVersion = bugfixVersion;
	}

	public int getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(int buildVersion) {
		this.buildVersion = buildVersion;
	}

	@Override
	public String toString() {
		return majorVersion + "." + featureVersion + "." + bugfixVersion;
	}

	public String toStringShort() {
		return majorVersion + "." + featureVersion;
	}

	public String toStringFull() {
		return toString() + "." + buildVersion;
	}

	/**
	 * 
	 * @param versionString
	 * @return parsed version, or null if versionString is unparsable or null
	 */
	public static ModVersion parse(String versionString) {
		if (versionString == null) {
			return null;
		}

		String[] parts = versionString.trim().split(".");
		if (parts.length > 4 || parts.length < 0) {
			return null;
		}
		for (String part : parts) {
			if (!part.matches("\\d+")) {
				return null;
			}
		}

		ModVersion parsed = new ModVersion();
		if (parts.length >= 1) {
			parsed.setMajorVersion(Integer.parseInt(parts[0]));
		}

		if (parts.length >= 2) {
			parsed.setFeatureVersion(Integer.parseInt(parts[1]));
		}

		if (parts.length >= 3) {
			parsed.setBugfixVersion(Integer.parseInt(parts[2]));
		}

		if (parts.length >= 4) {
			parsed.setBuildVersion(Integer.parseInt(parts[3]));
		}

		return parsed;
	}

	/**
	 * Compares two versions.
	 * 
	 * @param ver1
	 * @param ver2
	 * @return greater version, or null if ver1 or ver2 is null. If one is null,
	 *         returns the non-null argument.
	 */
	public ModVersion compare(ModVersion ver1, ModVersion ver2) {
		if (ver1 == null && ver2 == null) {
			return null;
		}

		if (ver1 == null && ver2 != null) {
			return ver2;
		}

		if (ver1 != null && ver2 == null) {
			return ver1;
		}

		if (ver1.getMajorVersion() > ver2.getMajorVersion()) {
			return ver1;
		} else if (ver1.getMajorVersion() < ver2.getMajorVersion()) {
			return ver2;
		}

		if (ver1.getFeatureVersion() > ver2.getFeatureVersion()) {
			return ver1;
		} else if (ver1.getFeatureVersion() < ver2.getFeatureVersion()) {
			return ver2;
		}

		if (ver1.getBugfixVersion() > ver2.getBugfixVersion()) {
			return ver1;
		} else if (ver1.getBugfixVersion() < ver2.getBugfixVersion()) {
			return ver2;
		}

		if (ver1.getBuildVersion() > ver2.getBuildVersion()) {
			return ver1;
		} else if (ver1.getBuildVersion() < ver2.getBuildVersion()) {
			return ver2;
		}

		return null;
	}
}
