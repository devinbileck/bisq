#!/usr/bin/env python3
# Refer to the following for more examples: https://github.com/koalalorenzo/python-digitalocean

import digitalocean


def create_droplet(api_token):
    droplet = digitalocean.Droplet(
        token=api_token,
        name="seednode",
        region="nyc3",
        image="ubuntu-18-04-x64",
        size_slug="2048mb",
        user_data="#cloud-config"
                  "users:"
                  "- name: bisq"
                  "ssh-authorized-keys:"
                  "- ssh-rsa AAAAB3NzaC1yc2EAAAABJQAAAQEAiUUQ8cnOo57g/em14BXUe4QbvM36Px6mAr0lq5ldkf0AhH3IFLGS1Z/w3lI2Z1/TvCjdEgpU7e4Z12vBNRtSvx1hf0sSE2+f/rpgPyAPQV48QAsdenYf9s19aJiOTvRyrF3wTILlCXxsLNY/Vd7nZPsv5tZD78THgrOwUKKh4O7oR6W7Nf3o0YVfA+e0YelqRO4P8/MJbBUuxndnOPiboTlk2L6vttWsvJKrzJcZ+b+89eZCK6GBlvSxk86TJmH4RVxX/qF+OZI/B2Gvw03o8VPWPczDUufpH+u5jPaywROq73s2+iylVjr3vCWZtUVBc5yy2/su6364tk5WUv5jcw== devin"
                  "- ssh-rsa AAAAB3NzaC1yc2EAAAABJQAAAQEAh1Ueub7G5vwmgpVVNhvdR1QRWiZIDza2Le70Vrsusy3Y/RLXTw4h5GYX7lnV6Cwuor4UjPpnfhs2z/CCHTUK0RU2BFuN+I+5yDIM/teSffA7+jyNpjmC3KEcpn5ZSw4dsCCd0n+uDWPelgOOhYeNiwACOOtv5qkOzCGNoFvDAowTxsBbFjpmdqnQBtsgf7pyjFon0iKErVhyRsB5jqLUNvD85ZOvkRNE9elCalcMNGoLauII/3LyOqXzfpuhqRYFWiCD/r/3D+QnPXLxmWRA7OzOmeVEkI9BtHKID/FAzOkhJ2YmMuFjaLKcSBbYBq2d9UYpLggHCcd6JEokJnjPlw== devin"
                  "sudo: ['ALL=(ALL) NOPASSWD:ALL']"
                  "groups: sudo"
                  "shell: /bin/bash"
                  "packages:"
                  "- unzip"
                  "runcmd:"
                  "- sed -i -e '/^PermitRootLogin/s/^.*$/PermitRootLogin no/' /etc/ssh/sshd_config"
                  "- sed -i -e '$aAllowUsers bisq' /etc/ssh/sshd_config"
                  "- restart ssh"
                  "- cd /home/bisq"
                  "- git clone https://github.com/devinbileck/bisq.git"
                  "- cd bisq"
                  "- git checkout deployment"
                  "- chmod +x scripts/*.sh"
                  "- ./scripts/install_java.sh"
                  "- ./gradlew build"
                  "- cd /home/bisq"
                  "- su -c /home/bisq/bisq/scripts/install_bitcoind.sh - bisq"
                  "- su -c /home/bisq/bisq/scripts/setup_dao.sh - bisq"
                  "- su -c \"bitcoind -daemon\" - bisq"
                  "- su -c /home/bisq/bisq/scripts/run_seednode.sh - bisq &")
    droplet.create()
    actions = droplet.get_actions()
    for action in actions:
        action.load()
        # Once it shows complete, droplet is up and running
        print(action.status)


if __name__ == '__main__':
    token = input('API token: ')
    create_droplet(token)
