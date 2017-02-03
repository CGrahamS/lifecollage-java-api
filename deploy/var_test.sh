#!/usr/bin/env bash
status=" Updating asdf23qewrd"

echo STATUS = $status

if [[ -n $status  ]]
then
    echo first test PASS
    else
    echo first test FAIL
fi

if [[ ! -z $status ]]
then
    echo second test PASS
    else
    echo second test FAIL
fi

if [[ $status != *"Already up-to-date."* ]]
then
    echo third test PASS
    else
    echo third test FAIL
fi

if [[ $status == *"Updating"* ]]
then
    echo fourth test PASS
    else
    echo fourth test FAIL
fi

if [[ -n $status && ! -z $status && $status != *"Already up-to-date."* && $status == *"Updating"* ]]
then
    echo CHANGES DETECTED
else
    echo NO CHANGSES
fi