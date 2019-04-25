#!/bin/bash

function build_images() {
	local path=$1
	local tag=$2
	local log_file=$4
	local pattern=$3
	local services=`ls ${path} | egrep  "${pattern}"`

	for service in $services; do
		echo "start build image ${service}:${tag}"
		build_image "${path}" "${service}" "${tag}" "${log_file}"
		RESULT=$?
		if [ ${RESULT} -ne 0 ]; then
			echo "${RED}build image ${service}:${tag} failure, please check log file [${log_file}] for details"
		else
			echo "build image ${service}:${tag} succeed"
		fi
	done	
}

function build_image() {
	local path=$1
	local image_name=$2
	local image_tag=$3
	local log_file=$4
	docker build -t "${image_name}:${image_tag}" -f "${path}/${image_name}/target/Dockerfile" "${path}" >> "${log_file}" 2>&1
	return $?
}

function push_images() {
	echo "${RED}Before push image, you should login remote repository, are you already login? y/n"
	read confirm
	if [ "${confirm}" != "y" ]; then
		echo "bye"
		exit 0
	fi

	local path=$1
	local tag=$2
	local remote_repo=$3
	local pattern=$4
	local log_file=$5
	local services=`ls ${path} | egrep "${pattern}"`
	for service in ${services}; do
		echo "start push image ${service}:${tag}"
		image_id=`docker images -q "${service}:${tag}"`
		if [ -z "${image_id}" ]; then
			echo "image ${service}:${tag} does not exist"
			continue
		fi
		echo "tag image ${service}:${tag} with tag ${remote_repo}/${service}:${tag}"
		docker tag "${image_id}" "${remote_repo}/${service}:${tag}"
		if [ $? -ne 0 ]; then
			echo "${RED}tag image ${service}:${tag} [with id ${image_id}] failure"
			continue
		fi
		echo "push image ${remote_repo}/${service}:${tag}"
		docker push "${remote_repo}/${service}:${tag}" >> ${log_file} 2>&1
		if [ $? -eq 0 ]; then
			echo "push image ${remote_repo}/${service}:${tag} succeed"
		else
			echo "${RED}push image ${remote_repo}/${service}:${tag} failure"
		fi
	done
}

if [ $# -lt 2 ]; then
	echo "usage: $0 <build|push> <tag> [service pattern] [remote repo]"
	exit 1
fi

RED='\033[0;31m'
NC='\033[0m'

CMD=$1
TAG=$2
SERVICES_PATTERN=$3
SERVICES_PATTERN="${SERVICES_PATTERN:-"*-service|*-website|loadtestclient|tx-coordinator|init-db"}"
REMOTE_REPO=$4
REMOTE_REPO="${REMOTE_REPO:-"100.125.0.198:20202/maoxuepeng6459"}"

RELATIVE_PATH="`dirname $0`"
cd "${RELATIVE_PATH}"
ABSOLUTELY_PATH=`pwd`
LOG_FILE="${ABSOLUTELY_PATH}/build-images.log"

if [ "${CMD}" = "build" ]; then
	build_images "${ABSOLUTELY_PATH}" "${TAG}"  "${SERVICES_PATTERN}" "${LOG_FILE}"
	exit $?
elif [ "${CMD}" = "push" ]; then
	push_images "${ABSOLUTELY_PATH}" "${TAG}" "${REMOTE_REPO}"  "${SERVICES_PATTERN}" "${LOG_FILE}"
	exit $?
else
	echo "Unknown command ${CMD}"
	exit 1
fi
