#!/bin/bash

echo "running install.."

SCRIPT_PATH=$(pwd)
RESOURCE=icarus-ide

XDG_DESKTOP_DIR="${HOME}/Desktop"
if [[ -f "${XDG_CONFIG_HOME:-${HOME}/.config}/user-dirs.dirs" ]]; then
  . "${XDG_CONFIG_HOME:-${HOME}/.config}/user-dirs.dirs"
fi

xdg_exists() {
	# TODO:  <10-04-19, tiago> # add xdg stuff
	return 1 
}


xdg_install() {
	# TODO:  <10-04-19, tiago> # add xdg install
	printf
}

simple_install() {
	TEMP_DIR=$(mktemp --directory)

	sed -e "s,<BIN_DIR>,${SCRIPT_PATH}/bin/devenv,g" \
		-e "s,<ICON_FILE>,${SCRIPT_PATH}/config/icons/icarus-128.png,g" "${SCRIPT_PATH}/config/template.desktop" > "${TEMP_DIR}/${RESOURCE}.desktop"

	mkdir -p "${HOME}/.local/share/applications"
	cp "${TEMP_DIR}/${RESOURCE}.desktop" "${HOME}/.local/share/applications/"

	mkdir -p "${HOME}/.local/share/metainfo"
	cp "${SCRIPT_PATH}/config/appdata.xml" "${HOME}/.local/share/metainfo/${RESOURCE}.appdata.xml"

	# TODO: <10-04-19, tiago> # xdg
	if [[ -d "${XDG_DESKTOP_DIR}" ]]; then
		cp "${TEMP_DIR}/${RESOURCE}.desktop" "${XDG_DESKTOP_DIR}/"
		chmod u+x "${XDG_DESKTOP_DIR}/${RESOURCE}.desktop"
	fi

	rm "${TEMP_DIR}/${RESOURCE}.desktop"
	rmdir "${TEMP_DIR}"
}

display_help() {
	printf "\nOptional arguments are:\n"
}

update_database() {
	if [[ -d "${HOME}/.local/share/applications" ]]; then
		if command -v update-desktop-database > /dev/null ; then
			update-desktop-database "${HOME}/.local/share/applications"
		fi
	fi

	if [[ -d "${HOME}/.local/share/mime" ]]; then
		if command -v update-mime-database > /dev/null ; then
			update-mime-database "${HOME}/.local/share/mime"
		fi
	fi
}

while [[ $# -gt 0 ]]; do
	case $1 in
		-a|--all)
			echo "first"
			shift
		;;
		*)
			echo "Invalid option -- '$1'"
			display_help
			exit 1
	esac
done

if  xdg_exists $1 ; then
	printf "Installing Icarus Project (xdg)...\n"
else
	printf "Installing Icarus Project...\n"
	simple_install
fi
update_database
printf "finish!\n"

exit 0
