## NoteTakingApp

---

#### Notes:

- Since AWS Elastic IPs for EC2 instances are paid, it will be
needed to change the Jenkins IP on Github repository webhook manually
everytime the EC2 instance is run and gets a new IP.
- If needed, go to Github to redeliver the content of the webhook to EC2.
- Github auth fails if git is not installed on EC2.
- Build fails if tools like Maven are not installed on EC2.

---

#### TODO:

- Continue configuring pipeline in Jenkins and complete Jenkinsfile
  https://community.jenkins.io/t/jenkins-not-able-to-connect-to-git-repository/8707/8
- Jenkins could send email when build fails and set status on Github after build
- See Jenkins nodes or similar (builds stuck in queue)
