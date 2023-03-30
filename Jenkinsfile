pipeline {
    agent any
    
    stages {
        stage('Building and create .jar file'){
            steps {
                echo 'Building the .jar file'
                
                //Builds and create our .jar file
                sh 'mvn clean package'
            }
        }
        
        stage('Creating Docker image') {
            steps {
                //Removes any extra docker images
                sh 'sudo docker image prune -f'
                
                //Builds the image of our application
                sh 'sudo docker build -t connoreg/p0refactored:latest ./Dockerfile'
            }
        }

        stage('Deploying into docker container') {
            steps {
                //Stop any running containers of this image
                sh 'sudo docker rm -f $(sudo docker ps -f name=p0refactored -q)'
                
                //Run latest version of image in a container
                sh 'sudo docker run -p 7475:8080 -e url=$proj0url --name p0refactored connoreg/p0refactored:latest'
            }
        }
    }
}